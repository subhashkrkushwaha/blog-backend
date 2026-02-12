package com.example.blog.controller;

import com.example.blog.dto.CategoryDto;
import com.example.blog.service.CategoryService;
import com.example.blog.util.JWTUtility;
import com.example.blog.util.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false) // disable JWT
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    // Required mocks for security
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JWTUtility jwtUtility;

    // ==========================================
    // CREATE CATEGORY
    // ==========================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateCategory() throws Exception {

        CategoryDto dto = new CategoryDto(1, "Tech");

        Mockito.when(categoryService.createCategory(any())).thenReturn(dto);

        mockMvc.perform(post("/ADMIN/category/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "category": "Tech"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.category").value("Tech"));
    }

    // ==========================================
    // UPDATE CATEGORY
    // ==========================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateCategory() throws Exception {

        CategoryDto dto = new CategoryDto(1, "Updated Category");

        Mockito.when(categoryService.updateCategory(eq(1), any()))
                .thenReturn(dto);

        mockMvc.perform(put("/ADMIN/category/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "category": "Updated Category"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Updated Category"));
    }

    // ==========================================
    // GET ALL CATEGORIES
    // ==========================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllCategories() throws Exception {

        List<CategoryDto> list = List.of(
                new CategoryDto(1, "Tech"),
                new CategoryDto(2, "Science")
        );

        Mockito.when(categoryService.getAllCategory()).thenReturn(list);

        mockMvc.perform(get("/ADMIN/category/get-all"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].category").value("Tech"))
                .andExpect(jsonPath("$[1].category").value("Science"));
    }

    // ==========================================
    // GET CATEGORY BY ID
    // ==========================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetByIdCategory() throws Exception {

        CategoryDto dto = new CategoryDto(1, "Tech");

        Mockito.when(categoryService.getByIdCategory(1)).thenReturn(dto);

        mockMvc.perform(get("/ADMIN/category/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Tech"));
    }

    // ==========================================
    // DELETE CATEGORY BY ID
    // ==========================================
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteByIdCategory() throws Exception {

        Mockito.doNothing().when(categoryService).deleteByIdCategory(1);

        mockMvc.perform(delete("/ADMIN/category/delete-by-id/1"))
                .andExpect(status().isNoContent());
    }
}
