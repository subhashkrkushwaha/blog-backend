package com.example.blog.dto.auth;

import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {

    private String token;
    private String userEmail;
//    private String role;
      @ManyToMany
      private List<String> userRoles;

}

