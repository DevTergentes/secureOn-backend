package com.secureon.IAM.dto;

import com.secureon.IAM.model.valueobjects.UserRole;
import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String fullName;
    private String email;
    private String password;
    private UserRole role;
}
