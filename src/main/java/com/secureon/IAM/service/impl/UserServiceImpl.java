package com.secureon.IAM.service.impl;

import com.secureon.IAM.dto.SigninRequest;
import com.secureon.IAM.dto.SignupRequest;
import com.secureon.IAM.dto.UserDTO;
import com.secureon.IAM.model.User;
import com.secureon.IAM.model.valueobjects.UserRole;
import com.secureon.IAM.repository.UserRepository;
import com.secureon.IAM.service.UserService;
import com.secureon.domain.model.Employee;
import com.secureon.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public String signup(SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent() ||
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Username or email already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role(request.getRole())
                .password(request.getPassword()) // ⚠️ NO uses plain text en producción
                .build();

        user = userRepository.save(user);

        if (request.getRole() == UserRole.EMPLOYEE) {
            Employee employee = Employee.builder()
                    .userId(user.getId())
                    .fullName(request.getFullName())
                    .build();
            employeeRepository.save(employee);
        }

        return "User registered successfully!";
    }

    @Override
    public UserDTO signin(SigninRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isPresent() && request.getPassword().equals(userOpt.get().getPassword())) {
            User user = userOpt.get();
            return new UserDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name()
            );
        }

        throw new RuntimeException("Invalid username or password");
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole().name());
        return dto;
    }
}
