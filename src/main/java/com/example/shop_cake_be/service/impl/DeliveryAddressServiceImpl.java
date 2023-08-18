package com.example.shop_cake_be.service.impl;

import com.example.shop_cake_be.common.Page;
import com.example.shop_cake_be.models.DeliveryAddress;
import com.example.shop_cake_be.models.User;
import com.example.shop_cake_be.payload.DeliveryAddressPayload;
import com.example.shop_cake_be.repository.DeliveryAddressRepo;
import com.example.shop_cake_be.repository.UserRepository;
import com.example.shop_cake_be.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {
    @Autowired
    DeliveryAddressRepo repo;
    @Autowired
    UserRepository userRepository;
    @Override
    public DeliveryAddress create(DeliveryAddress address) {
        // lấy thông tin user đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        // end

        List<DeliveryAddress> addresses = repo.findByUserIdAndStatus(user.get().getId(),1);
        if(!addresses.isEmpty()) {
            for (DeliveryAddress d : addresses) {
                d.setStatus(0);
                repo.save(d);
            }
        }

        DeliveryAddress deliveryAddress = new DeliveryAddress();
        deliveryAddress.setUserId(user.get().getId());
        deliveryAddress.setAddress(address.getAddress());
        deliveryAddress.setPhone(address.getPhone());
        deliveryAddress.setName(address.getName());
        deliveryAddress.setIsDeleted(1);
        deliveryAddress.setStatus(1);
        repo.save(deliveryAddress);
        return deliveryAddress;
    }

    @Override
    public Page<Object> getAllAndSearch(DeliveryAddressPayload filter) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        filter.setUserId(user.get().getId());
        org.springframework.data.domain.Page<DeliveryAddress> page = repo.getAllAndSearch(filter, PageRequest.of(filter.getPage() - 1, filter.getSize()));
        if(page.isEmpty()) {
            return null;
        }
        List<Object> objectList = new ArrayList<>(page.getContent());
        return com.example.shop_cake_be.common.Page.builder()
                .result(objectList)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .pageSize(filter.getSize())
                .pageNumber(filter.getPage())
                .build();
    }
    @Override
    public Optional<DeliveryAddress> findById(long id) {
        Optional<DeliveryAddress> da = repo.findById(id);
        if(da.isEmpty()) return Optional.empty();
        return da;
    }

    @Override
    public Optional<DeliveryAddress> update(long id, DeliveryAddress model) {
        Optional<DeliveryAddress> da = repo.findById(id);
        if(da.isEmpty()) return Optional.empty();
        da.get().setAddress(model.getAddress());
        da.get().setPhone(model.getPhone());
        da.get().setStatus(model.getStatus());
        da.get().setName(model.getName());
        repo.save(da.get());
        return da;
    }

    @Override
    public boolean delete(long id) {
        Optional<DeliveryAddress> da = repo.findById(id);
        if(da.isEmpty()) return false;
        da.get().setIsDeleted(0);
        repo.save(da.get());
        return true;
    }

    @Override
    public boolean procedure(long id, DeliveryAddressPayload payload) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        Optional<DeliveryAddress> da = repo.findById(id);
        if(da.isEmpty()) return false;
        switch (payload.getProcedure()) {
            case "hoạt động":
                List<DeliveryAddress> addresses = repo.findByUserIdAndStatus(user.get().getId(),1);
                if (!addresses.isEmpty()) {
                    for (DeliveryAddress deliveryAddress : addresses){
                        deliveryAddress.setStatus(0);
                        repo.save(deliveryAddress);
                    }
                }
                da.get().setStatus(1);
                repo.save(da.get());
                break;
            default:
                da.get().setStatus(0);
                repo.save(da.get());
                break;
        }
        return true;
    }

    @Override
    public List<DeliveryAddress> findByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        List<DeliveryAddress> das = repo.findByUserId(user.get().getId());
        if(das.isEmpty()) return null;
        return das;
    }
}
