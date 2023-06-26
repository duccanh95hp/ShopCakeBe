package com.example.shop_cake_be.repository;

import com.example.shop_cake_be.models.ERole;
import com.example.shop_cake_be.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}