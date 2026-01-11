package com.gl.authentication.service;

import com.gl.authentication.dto.CreateUserRequest;
import com.gl.authentication.dto.UserResponse;

import java.util.List;

public interface UserService {

    com.gl.authentication.dto.UserResponse createUser(CreateUserRequest request);

    UserResponse getById(String id);

    List<UserResponse> getAll();
}
