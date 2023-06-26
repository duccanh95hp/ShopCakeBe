package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.dto.OrderDetailDto;
import com.example.shop_cake_be.dto.OrderDto;
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

import java.time.LocalDateTime;
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
            order.setStatus(model.getStatus());
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
            User user = userRepository.findById(order.getId()).get();
            orderDto.setId(order.getId());
            orderDto.setName(user.getUsername());
            orderDto.setEmail(user.getEmail());
            orderDto.setOrderCode(order.getOrderCode());
            DeliveryAddress deliveryAddress = deliveryAddressRepo.findById(order.getDeliveryAddressId()).get();
            orderDto.setDeliveryAddress(deliveryAddress.getAddress());
            orderDto.setPhone(deliveryAddress.getPhone());
            orderDto.setStatus(order.getStatus());
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
    public List<OrderDetailDto> detailOrder(long orderId) {
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
        return orderDetailDtos;
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
                orderRepo.save(order.get());
                break;
            default:
                order.get().setStatus(1);
                orderRepo.save(order.get());
                break;
        }
        return true;
    }

}
