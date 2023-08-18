package com.example.shop_cake_be.controller;

import com.example.shop_cake_be.dto.UserDto;
import com.example.shop_cake_be.models.ERole;
import com.example.shop_cake_be.models.Role;
import com.example.shop_cake_be.models.User;
import com.example.shop_cake_be.payload.UserPayload;
import com.example.shop_cake_be.payload.reponse.JwtResponse;
import com.example.shop_cake_be.payload.reponse.MessageResponse;
import com.example.shop_cake_be.payload.request.LoginRequest;
import com.example.shop_cake_be.payload.request.SignupRequest;
import com.example.shop_cake_be.repository.RoleRepository;
import com.example.shop_cake_be.repository.UserRepository;
import com.example.shop_cake_be.security.jwt.JwtUtils;
import com.example.shop_cake_be.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @GetMapping("/detail")
    public ResponseEntity<?> detail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.get().getId());
        userDto.setUsername(user.get().getUsername());
        userDto.setAddress(user.get().getAddress());


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if(user.get().getBirthday() != null) {
            userDto.setBirthday(user.get().getBirthday().format(formatter));
        }
        userDto.setEmail(user.get().getEmail());
        userDto.setTelephone(user.get().getTelephone());
        userDto.setFullName(user.get().getFullName());
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserPayload payload) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new UserDto();
        try
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            LocalDate localDate = LocalDate.parse(payload.getBirthday(), formatter);
            user.get().setAddress(payload.getAddress());
            user.get().setBirthday(localDate);
            user.get().setFullName(payload.getFullName());
            user.get().setTelephone(payload.getTelephone());
            userRepository.save(user.get());

            userDto.setId(user.get().getId());
            userDto.setUsername(user.get().getUsername());
            userDto.setAddress(user.get().getAddress());

            userDto.setBirthday(payload.getBirthday());
            userDto.setEmail(user.get().getEmail());
            userDto.setTelephone(user.get().getTelephone());
            userDto.setFullName(user.get().getFullName());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userDto);
    }
}
