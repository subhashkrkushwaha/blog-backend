package com.example.blog.controller;

import com.example.blog.dto.UserDto;
import com.example.blog.entity.User;
import com.example.blog.service.UserDetailsServiceImpl;
import com.example.blog.service.UserService;
import com.example.blog.util.JWTUtility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

import static com.example.blog.constants.ErrorMessages.INVALID_CREDENTIALS;

@RestController
@RequestMapping("/auth")
@Tag(name="AUTH API")//swagger
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtility  jwtUtility;
    //login
    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequestDto) {

        User user = userService.loadByEmail(loginRequestDto.get("userEmail"));
        String email = loginRequestDto.get("userEmail");
        String password = loginRequestDto.get("password");
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException(INVALID_CREDENTIALS);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtility.generateToken(userDetails);

        return ResponseEntity.ok(Collections.singletonMap("token", token));

    }
    //register
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto userDto1 = userService.createUser(userDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(userDto1);
    }
}
