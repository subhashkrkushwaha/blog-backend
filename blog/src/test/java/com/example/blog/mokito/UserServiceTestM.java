package com.example.blog.mokito;

import com.example.blog.dto.UserDto;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.entity.UserRole;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTestM {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserService userService;

    List<UserRole> role = new ArrayList<>();
    List<Post> post = new ArrayList<>();

    @Test
    void TestGetByIdUser() {
        User user = new User(10, "Seema kushwaha", "satya@gmail.com", "password",
                LocalDateTime.now(), role, post);

        UserDto dto = new UserDto();
        dto.setUserName("Seema kushwaha");

        when(userRepository.findById(10)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(dto);

        UserDto result = userService.getByIdUser(10);

        assertNotNull(result);
        assertEquals("Seema kushwaha", result.getUserName());
    }

    @Test
    void testGetByIdUser_simple() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(new UserDto());

        UserDto result = userService.getByIdUser(1);

        assertNotNull(result);
    }

    @Test
    void testGetByIdUser_notFound() {
        when(userRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.getByIdUser(100));
    }

    @Test
    void testGetAllUser() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(any(), eq(UserDto.class))).thenReturn(new UserDto());

        List<UserDto> list = userService.getAllUser();

        assertEquals(1, list.size());
    }

    @Test
    void testLoadByEmail() {
        User user = new User();
        when(userRepository.findByUserEmail("test@gmail.com")).thenReturn(Optional.of(user));

        assertNotNull(userService.loadByEmail("test@gmail.com"));
    }

    @Test
    void testLoadByEmail_notFound() {
        when(userRepository.findByUserEmail("unknown@gmail.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.loadByEmail("unknown@gmail.com"));
    }

    @Test
    void testDeleteByIdUser() {
        when(userRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1);

        assertDoesNotThrow(() -> userService.deleteByIdUser(1));
    }

    @Test
    void testDeleteByIdUser_notFound() {
        when(userRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteByIdUser(99));
    }

    @Test
    void testOwnAccountDelete() {
        User user = new User();
        when(userRepository.findByUserEmail("abc@gmail.com"))
                .thenReturn(Optional.of(user));

        doNothing().when(userRepository).delete(user);

        assertDoesNotThrow(() -> userService.ownAccountDelete("abc@gmail.com"));
    }

    @Test
    void testGetId() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        assertNotNull(userService.getId(1));
    }
}
