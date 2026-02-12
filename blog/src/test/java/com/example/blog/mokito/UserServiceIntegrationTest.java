package com.example.blog.mokito;

import com.example.blog.dto.UserDto;
import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @Disabled
    @Test
    void testGetUserFromDB() {
        User saved = new User();
        saved.setUserName("John");
        saved.setUserEmail("john@gmail.com");
        saved.setPassword("pass");

        userRepository.save(saved);

        UserDto dto = userService.getByIdUser(saved.getId());

        assertEquals("John", dto.getUserName());
    }
}
