package com.example.blog.controller;

import com.example.blog.dto.UserDto;
import com.example.blog.service.UserService;
import com.example.blog.util.JWTUtility;
import com.example.blog.util.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTUtility jwtUtility;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    void testCreateUser() throws Exception {

        UserDto dto = new UserDto(1, "test@gmail.com", "John");

        when(userService.createUser(any())).thenReturn(dto);

        mockMvc.perform(post("/USER/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 1,
                                    "userEmail": "test@gmail.com",
                                    "userName": "John"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userEmail").value("test@gmail.com"))
                .andExpect(jsonPath("$.userName").value("John"));
    }


    @WithMockUser(username = "user@gmail.com", roles = {"USER"})
    @Test
    void testUpdateUser() throws Exception {

        UserDto updated = new UserDto(1, "user@gmail.com", "Updated");

        when(userService.updateUser(any(), eq("user@gmail.com")))
                .thenReturn(updated);

        mockMvc.perform(put("/USER/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 1,
                                    "userEmail": "user@gmail.com",
                                    "userName": "Updated"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("Updated"));
    }


    @WithMockUser(roles = {"ADMIN"})
    @Test
    void testGetAllUsers() throws Exception {

        List<UserDto> list = List.of(
                new UserDto(1, "a@gmail.com", "A"),
                new UserDto(2, "b@gmail.com", "B")
        );

        when(userService.getAllUser()).thenReturn(list);

        mockMvc.perform(get("/USER/get-all"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].userEmail").value("a@gmail.com"))
                .andExpect(jsonPath("$[1].userEmail").value("b@gmail.com"));
    }


    @WithMockUser(roles = {"ADMIN", "USER"})
    @Test
    void testGetByIdUser() throws Exception {

        UserDto dto = new UserDto(1, "user@gmail.com", "John");

        when(userService.getByIdUser(1)).thenReturn(dto);

        mockMvc.perform(get("/USER/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userEmail").value("user@gmail.com"));
    }


    @WithMockUser(roles = {"ADMIN"})
    @Test
    void testDeleteByIdUser() throws Exception {

        doNothing().when(userService).deleteByIdUser(1);

        mockMvc.perform(delete("/USER/delete-by-id/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteByIdUser(1);
    }


    @WithMockUser(username = "self@gmail.com", roles = {"USER"})
    @Test
    void testOwnAccountDelete() throws Exception {

        doNothing().when(userService).ownAccountDelete("self@gmail.com");

        mockMvc.perform(delete("/USER/own-account-delete"))
                .andExpect(status().isNoContent());

        verify(userService).ownAccountDelete("self@gmail.com");
    }
}
