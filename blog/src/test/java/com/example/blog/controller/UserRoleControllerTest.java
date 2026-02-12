package com.example.blog.controller;

import com.example.blog.config.SecurityConfig;
import com.example.blog.dto.UserRoleDto;
import com.example.blog.service.UserRoleService;
import com.example.blog.util.JWTUtility;
import com.example.blog.util.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(UserRoleController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRoleService userRoleService;
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JWTUtility jwtUtility;

    // Add a fake admin user for @PreAuthorize
    @WithMockUser(roles = "ADMIN")
    @Test
    void testCreateUserRole() throws Exception {

        UserRoleDto dto = new UserRoleDto(1, "ADMIN");

        when(userRoleService.createUserRole(any())).thenReturn(dto);

        mockMvc.perform(post("/ADMIN/create-role")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                   "id": 1,
                                   "roleName": "ADMIN"
                                 }
                                 """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.roleName").value("ADMIN"));

        verify(userRoleService, times(1)).createUserRole(any());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testUpdateUserRole() throws Exception {

        UserRoleDto dto = new UserRoleDto(1, "UPDATED_ROLE");

        when(userRoleService.updateUserRole(eq(1), any())).thenReturn(dto);

        mockMvc.perform(put("/ADMIN/update-role/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                 {
                                   "id": 1,
                                   "roleName": "UPDATED_ROLE"
                                 }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("UPDATED_ROLE"));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testGetByIdUserRole() throws Exception {
        UserRoleDto dto = new UserRoleDto(1, "ADMIN");

        when(userRoleService.getByIdUserRoles(1)).thenReturn(dto);

        mockMvc.perform(get("/ADMIN/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("ADMIN"))
                .andExpect(jsonPath("$.id").value(1));
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void testDeleteUserRole() throws Exception {

        doNothing().when(userRoleService).deleteByIdUserRoles(1);

        mockMvc.perform(delete("/ADMIN/delete-by-id/1"))
                .andExpect(status().isNoContent());

        verify(userRoleService, times(1)).deleteByIdUserRoles(1);
    }
}
