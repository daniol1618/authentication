package com.gl.authentication.service.impl;

import com.gl.authentication.dto.CreateUserRequest;
import com.gl.authentication.dto.PhoneRequest;
import com.gl.authentication.dto.UserResponse;
import com.gl.authentication.entity.Phone;
import com.gl.authentication.entity.User;
import com.gl.authentication.exception.UserAlreadyExistsException;
import com.gl.authentication.repository.UserRepository;
import com.gl.authentication.security.JwtProvider;
import com.gl.authentication.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("El usuario ya existe");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());

        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now);
        user.setLastLogin(now);
        user.setActive(true);

        if (request.getPhones() != null) {
            List<Phone> phones = request.getPhones().stream()
                    .map(this::mapToPhone)
                    .collect(Collectors.toList());
            user.setPhones(phones);
        }

        User saved = userRepository.save(user);

        String token = jwtProvider.generateToken(saved.getEmail());

        return mapToResponse(saved, token);
    }

    @Override
    public UserResponse getById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return mapToResponse(user, null);
    }

    @Override
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> mapToResponse(u, null))
                .collect(Collectors.toList());
    }

    private Phone mapToPhone(PhoneRequest pr) {
        Phone p = new Phone();
        p.setNumber(pr.getNumber());
        p.setCityCode(pr.getCityCode());
        p.setCountryCode(pr.getCountryCode());
        return p;
    }

    private UserResponse mapToResponse(User user, String token) {
        UserResponse r = new UserResponse();
        r.setId(user.getId());
        r.setCreated(user.getCreated());
        r.setLastLogin(user.getLastLogin());
        r.setActive(user.isActive());
        r.setEmail(user.getEmail());
        r.setName(user.getName());
        r.setToken(token);

        if (user.getPhones() != null) {
            List<PhoneRequest> phones = user.getPhones().stream().map(p -> {
                PhoneRequest pr = new PhoneRequest();
                pr.setNumber(p.getNumber());
                pr.setCityCode(p.getCityCode());
                pr.setCountryCode(p.getCountryCode());
                return pr;
            }).collect(Collectors.toList());
            r.setPhones(phones);
        }

        return r;
    }
}
