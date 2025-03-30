package com.example.usermanagement.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String email;  // Used to find the user
    private String newUsername;
    private String newAddress;
    private String newPhoneNumber;
}

