package com.app.studentManagementSystem.services;

import com.app.studentManagementSystem.entity.Token;
import com.app.studentManagementSystem.entity.User;
import com.app.studentManagementSystem.exception.UserNotFoundException;
import com.app.studentManagementSystem.exception.UserRegisteredException;
import com.app.studentManagementSystem.model.request.RegisterRequest;
import com.app.studentManagementSystem.model.request.LoginRequest;
import com.app.studentManagementSystem.model.response.RegisterResponse;
import com.app.studentManagementSystem.model.response.LoginResponse;
import com.app.studentManagementSystem.repositories.TokenRepository;
import com.app.studentManagementSystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest registerRequest){
        User oldUser = userRepository.findUserByEmail(registerRequest.getEmail());
                if(oldUser != null){
                    throw new UserRegisteredException("User is already registered in database..change this email!!");
                }
        User user = User.builder()
                .userName(registerRequest.getUserName())
                .email(registerRequest.getEmail())
                .role(registerRequest.getRole())
                .password(bCryptPasswordEncoder.encode(registerRequest.getPassword()))
                .build();
        User savedUser = userRepository.save(user);
        return new RegisterResponse( registerRequest.getEmail(),"User Registered Successfully!!");
    }
    public LoginResponse login(LoginRequest loginRequest){
        User notUser = userRepository.findUserByEmail(loginRequest.getEmail());
        if(notUser == null){
            throw new UserNotFoundException("User is not registered in database..!!");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
        User user = userRepository.findUserByEmail(loginRequest.getEmail());
        String token = jwtService.createToken(user);
        System.out.println("token "+token);
        saveUserToken(user,token);
        return new LoginResponse(loginRequest.getEmail(),token);
    }
    public void saveUserToken(User user,String jwtToken){
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .build();
        tokenRepository.save(token);
    }
}
