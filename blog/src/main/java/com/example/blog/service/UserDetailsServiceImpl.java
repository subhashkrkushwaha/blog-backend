package com.example.blog.service;

import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(()-> new UsernameNotFoundException("User Email not found with username: " +userEmail));
              /*List<GrantedAuthority> authorities = user.getUserRoles()
                                                        .stream()
                                                        .map(userRole -> new SimpleGrantedAuthority(userRole.getUserRoleName()))
                                                                .collect(Collectors.toUnmodifiableList());
               return new org.springframework.security.core.userdetails.User(
                user.getUserEmail(),
                user.getPassword(),
                authorities
        );*/
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUserEmail())
                .password(user.getPassword())
                .authorities(
                        user.getUserRoles()
                                .stream()
                                .map(   userRole -> userRole.getUserRoleName() ) // ROLE_USER
                                .toArray(String[]::new)
                )
                .build();
    }
}
