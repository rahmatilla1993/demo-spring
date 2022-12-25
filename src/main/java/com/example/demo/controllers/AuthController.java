package com.example.demo.controllers;

import com.example.demo.payload.LoginDto;
import com.example.demo.payload.RegisterDto;
import com.example.demo.services.AuthService;
import com.example.demo.util.ApiResponse;
import com.example.demo.util.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ResponseErrorValidation responseErrorValidation;
    private final AuthService authService;

    @Autowired
    public AuthController(ResponseErrorValidation responseErrorValidation, AuthService authService) {
        this.responseErrorValidation = responseErrorValidation;
        this.authService = authService;
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody @Valid LoginDto loginDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseErrorValidation.mapValidationService(bindingResult);
        }
        ApiResponse response = authService.login(loginDto);
        return ResponseEntity
                .status(response.isSuccess()? OK : BAD_REQUEST)
                .body(response);
    }

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody @Valid RegisterDto registerDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseErrorValidation.mapValidationService(bindingResult);
        }
        ApiResponse response = authService.register(registerDto);
        return ResponseEntity
                .status(response.isSuccess() ? CREATED : CONFLICT)
                .body(response);
    }
}
