package com.example.usermanagement.service;

import com.example.usermanagement.dto.ForgotPasswordRequest;
import com.example.usermanagement.dto.ResetPasswordRequest;
import com.example.usermanagement.dto.UpdateProfileRequest;
import com.example.usermanagement.model.User;
import com.example.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //user signup
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // User Login (Plain text password check)
    public boolean authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getPassword().equals(password); // Plain text comparison
        }
        return false;
    }

    // Generate random OTP (6 digits)
    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // Handle Forgot Password Request (Send OTP)
    public String forgotPassword(ForgotPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String otp = generateOTP();  // Generate OTP
            user.setResetToken(otp);
            userRepository.save(user);

            // Here, you would send the OTP via email (not implemented in this step)
            return "OTP Sent to " + request.getEmail();
        } else {
            return "User not found!";
        }
    }

    // Reset Password Using OTP
    public String resetPassword(ResetPasswordRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getResetToken() != null && user.getResetToken().equals(request.getToken())) {
                user.setPassword(request.getNewPassword()); // Store plain password
                user.setResetToken(null); // Clear token after successful reset
                userRepository.save(user);
                return "Password Reset Successfully!";
            } else {
                return "Invalid OTP!";
            }
        } else {
            return "User not found!";
        }
    }

    public String updateProfile(UpdateProfileRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update only non-null fields
            if (request.getNewUsername() != null) {
                user.setUsername(request.getNewUsername());
            }
            if (request.getNewAddress() != null) {
                user.setAddress(request.getNewAddress());
            }
            if (request.getNewPhoneNumber() != null) {
                user.setPhoneNumber(request.getNewPhoneNumber());
            }

            userRepository.save(user);  // Save updated user
            return "Profile updated successfully!";
        } else {
            return "User not found!";
        }
    }

    public User getProfileByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null);  // Return user if found, else return null
    }

}

