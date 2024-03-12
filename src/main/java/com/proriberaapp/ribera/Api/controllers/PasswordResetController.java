package com.proriberaapp.ribera.Api.controllers;

import com.proriberaapp.ribera.Domain.entities.PasswordResetTokenEntity;
import com.proriberaapp.ribera.Domain.entities.UserEntity;
import com.proriberaapp.ribera.Infraestructure.services.PasswordResetTokenService;
import com.proriberaapp.ribera.Infraestructure.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        PasswordResetTokenEntity token = passwordResetTokenService.generateToken(user);
        return ResponseEntity.ok("Password reset code sent to email");
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyPasswordReset(@RequestParam String email, @RequestParam String code) {
        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        boolean isValid = passwordResetTokenService.verifyToken(user, code);
        if (isValid) {
            return ResponseEntity.ok("Password reset code is valid");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password reset code");
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPasswordReset(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword) {
        UserEntity user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        boolean isValid = passwordResetTokenService.verifyToken(user, code);
        if (isValid) {
            userService.updatePassword(user, newPassword);
            return ResponseEntity.ok("Password reset successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password reset code");
        }
    }
}