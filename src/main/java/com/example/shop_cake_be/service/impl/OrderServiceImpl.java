package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.dto.*;
import com.example.shop_cake_be.filter.OrderFilter;
import com.example.shop_cake_be.models.*;
import com.example.shop_cake_be.payload.OrderPayload;
import com.example.shop_cake_be.repository.*;
import com.example.shop_cake_be.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.shop_cake_be.common.Constants.*;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    OrderDetailRepo orderDetailRepo;
    @Autowired
    DeliveryAddressRepo deliveryAddressRepo;
    @Autowired
    CakeRepo cakeRepo;
    @Autowired
    ShoppingCartTmtRepo shoppingCartTmtRepo;
    @Override
    public Order create(OrderPayload model) {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.systemDefault());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(username);
            if (!user.isPresent()) {
                throw new UsernameNotFoundException("User not found");
            }
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String formattedDateTime = now.format(formatter);
            DateTimeFormatter formatterDe = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime localDateTime = LocalDateTime.parse(model.getDeliveryDate(), formatterDe);
            Order order = new Order();
            order.setUserId(user.get().getId());
            order.setDeliveryAddressId(model.getDeliveryAddressId());
            order.setOrderCode(formattedDateTime);
            order.setNote(model.getNote());
            order.setReason(model.getReason());
            order.setStatus(1);
            order.setDeliveryDate(localDateTime);
            order.setUpdatedAt(currentDateTime);
            order.setCreatedAt(currentDateTime);
            orderRepo.save(order);
            // thêm sản phẩm vào hóa đơn chi tiết
            for (OrderDetail orderDetail : model.getOrderDetails()) {
                OrderDetail detail = new OrderDetail();
                detail.setOrderId(order.getId());
                detail.setCakeId(orderDetail.getCakeId());
                detail.setPrice(orderDetail.getPrice());
                detail.setQuantity(orderDetail.getQuantity());
                orderDetailRepo.save(detail);
                Optional<ShoppingCartTmt> shoppingCartTmt = shoppingCartTmtRepo.findByUserIdAndCakeId(user.get().getId(), orderDetail.getCakeId());
                if(shoppingCartTmt.isPresent()) {
                    shoppingCartTmtRepo.delete(shoppingCartTmt.get());
                }
            }
            // set status deliveryAddress
            Optional<DeliveryAddress> deliveryAddress = deliveryAddressRepo.findById(model.getDeliveryAddressId());
            if(deliveryAddress.isPresent()) {
                List<DeliveryAddress> address = deliveryAddressRepo.findByUserIdAndStatus(user.get().getId(),1);
                if(!address.isEmpty()) {
                    for (DeliveryAddress d : address) {
                        d.setStatus(0);
                    }
                }
                deliveryAddress.get().setStatus(1);
            }
            return order;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Page<Object> getAllAndSearch(OrderPayload filter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> users = userRepository.findByUsername(username);
        if (!users.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        OrderFilter orderFilter = new OrderFilter();
        orderFilter.setSize(filter.getSize());
        orderFilter.setPage(filter.getPage());
        if(filter.getUserId() == 0) {
            orderFilter.setUserId(filter.getUserId());
        } else {
            orderFilter.setUserId(users.get().getId());
        }
        if(filter.getToDate() != null) {
            orderFilter.setToDate(LocalDateTime.parse(filter.getToDate(), formatter));
        }
        if(filter.getFromDate() != null) {
            orderFilter.setFromDate(LocalDateTime.parse(filter.getFromDate(), formatter));
        }
        orderFilter.setStatus(filter.getStatus());
        org.springframework.data.domain.Page<Order> page = orderRepo.getAllAndSearch(orderFilter, PageRequest.of(filter.getPage() - 1, filter.getSize()));
        if(page.isEmpty()) {
            return null;
        }
        List<Object> objectList = new ArrayList<>();

        for (Order order : page) {
            OrderDto orderDto = new OrderDto();
            User user = userRepository.findById(order.getUserId()).get();
            orderDto.setId(order.getId());
            orderDto.setName(user.getUsername());
            orderDto.setEmail(user.getEmail());
            orderDto.setOrderCode(order.getOrderCode());
            DeliveryAddress deliveryAddress = deliveryAddressRepo.findById(order.getDeliveryAddressId()).get();
            orderDto.setDeliveryAddress(deliveryAddress.getAddress());
            orderDto.setPhone(deliveryAddress.getPhone());
            orderDto.setStatus(order.getStatus());
            orderDto.setReason(order.getReason());
            orderDto.setDeliveryDate(order.getDeliveryDate());
            orderDto.setCreatedAt(order.getCreatedAt());
            objectList.add(orderDto);
        }
        return com.example.shop_cake_be.common.Page.builder()
                .result(objectList)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .pageSize(filter.getSize())
                .pageNumber(filter.getPage())
                .build();
    }

    @Override
    public OrderDetailStatusDto detailOrder(long orderId) {
        OrderDetailStatusDto orderDetailStatusDto = new OrderDetailStatusDto();
        Order order = orderRepo.findById(orderId).get();
        orderDetailStatusDto.setStatus(order.getStatus());
        List<OrderDetail> orderDetails = orderDetailRepo.findByOrderId(orderId);
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        if(orderDetails.isEmpty()) {
            return null;
        }
        for (OrderDetail orderDetail : orderDetails) {
            Cake cake = cakeRepo.findById(orderDetail.getCakeId()).get();
            OrderDetailDto orderDetailDto = new OrderDetailDto();
            orderDetailDto.setNameCake(cake.getName());
            orderDetailDto.setPrice(orderDetail.getPrice());
            orderDetailDto.setQuantity(orderDetail.getQuantity());
            orderDetailDto.setImage(cake.getImage());
            orderDetailDtos.add(orderDetailDto);
        }
        orderDetailStatusDto.setDetailDtoList(orderDetailDtos);
        return orderDetailStatusDto;
    }

    @Override
    public boolean procedure(long orderId, OrderPayload payload) {
        Optional<Order> order = orderRepo.findById(orderId);
        if(order.isEmpty()) return false;
        switch (payload.getProcedure()) {
            case APPROVE:
                order.get().setStatus(2);
                orderRepo.save(order.get());
                break;
            case TRANSPORT:
                order.get().setStatus(3);
                orderRepo.save(order.get());
                break;
            case COMPLETE:
                order.get().setStatus(4);
                orderRepo.save(order.get());
                break;
            case REJECT:
                order.get().setStatus(5);
                order.get().setReason(payload.getReason());
                orderRepo.save(order.get());
                break;
            case CANCEL:
                order.get().setStatus(5);
                order.get().setReason(payload.getReason());
                orderRepo.save(order.get());
                break;
            default:
                order.get().setStatus(1);
                orderRepo.save(order.get());
                break;
        }
        return true;
    }

    @Override
    public List<TopUserOrderDto> topUserOrderDto(OrderPayload payload) {
        OrderFilter filter = new OrderFilter();
        filter.setStatus(payload.getStatus());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime fromDay = LocalDateTime.of(LocalDate.parse(payload.getFromDate(), formatter), LocalTime.MIN);
        LocalDateTime toDay = LocalDateTime.of(LocalDate.parse(payload.getToDate(), formatter), LocalTime.MAX);
        filter.setFromDate(fromDay);
        filter.setToDate(toDay);
        List<TopUserOrderResultDto> objects = orderRepo.topUserByOrder(filter);
        if(objects.size() > payload.getLimit()) {
            objects = objects.subList(0,payload.getLimit());
        }
        List<TopUserOrderDto> topUserOrderDtoList = new ArrayList<>();
        Double count = 0d;
        for (TopUserOrderResultDto o : objects) {

            TopUserOrderDto topUserOrderDto = new TopUserOrderDto();
            Optional<User> user = userRepository.findById(o.getUserId());
            if(!user.isPresent()) {
                throw new UsernameNotFoundException("User not found");
            }
            topUserOrderDto.setIdUser(o.getUserId());
            topUserOrderDto.setAddress(user.get().getAddress());
            topUserOrderDto.setEmail(user.get().getEmail());
            topUserOrderDto.setPhone(user.get().getTelephone());
            topUserOrderDto.setName(user.get().getUsername());
            topUserOrderDto.setQuantity(o.getQuantity());
            List<Order> orders = orderRepo.findByUserIdAndStatus(o.getUserId(), payload.getStatus());
            for (Order order : orders) {
                List<OrderDetail> orderDetails = orderDetailRepo.findByOrderId(order.getId());
                for (OrderDetail orderDetail : orderDetails) {
                    count = count + (orderDetail.getPrice() * orderDetail.getQuantity());
                }
            }
            topUserOrderDto.setTotalMoney(count);

            topUserOrderDtoList.add(topUserOrderDto);
            count = 0d;
        }
        return topUserOrderDtoList;
    }
}
