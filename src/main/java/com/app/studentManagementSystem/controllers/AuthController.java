package com.app.studentManagementSystem.controllers;

import com.app.studentManagementSystem.model.request.RegisterRequest;
import com.app.studentManagementSystem.model.request.LoginRequest;
import com.app.studentManagementSystem.model.response.RegisterResponse;
import com.app.studentManagementSystem.model.response.LoginResponse;
import com.app.studentManagementSystem.services.AuthService;
import com.app.studentManagementSystem.services.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest/auth")
@Validated
@Tag(name="Authentication API",description = "APIs for managing authentication of the app")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping(value="/register")
    @Operation(summary = "Register specific user",description = "this API will register a new user unless he doesn't exist in database")
    @Parameter(name = "RegisterRequest",description = "this class consists of requested body for this API: userName,email,password & role",required = true)
    @ApiResponse(responseCode = "200",description = "Successful request")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.Register(registerRequest));
    }
    @PostMapping(value="/login")
    @Operation(summary = "Login specific user",description = "this API will login with an existing user in database")
    @Parameter(name = "LoginRequest",description = "this class consists of requested body for this API: email & password",required = true)
    @ApiResponse(responseCode = "200",description = "Successful request")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.Login(loginRequest));
    }
}
