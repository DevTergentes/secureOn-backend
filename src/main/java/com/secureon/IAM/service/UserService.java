package com.secureon.IAM.service;

import com.secureon.IAM.dto.SigninRequest;
import com.secureon.IAM.dto.SignupRequest;
import com.secureon.IAM.dto.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    String signup(SignupRequest request);
    UserDTO signin(SigninRequest request);
    List<UserDTO> getAllUsers();
    Optional<UserDTO> getUserById(Long id);
}