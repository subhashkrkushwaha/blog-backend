package com.example.blog.controller;


import com.example.blog.dto.PostDto;
import com.example.blog.service.PostService;
import com.example.blog.util.JWTUtility;
import com.example.blog.util.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc(addFilters = false)   // 🔥 Disable JWT filter
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    // 🔥 Required mocks for Spring Security
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JWTUtility jwtUtility;

    // ==========================================
    // CREATE POST
    // ==========================================
    @WithMockUser(username = "author@gmail.com", roles = {"AUTHOR"})
    @Test
    void testCreatePost() throws Exception {

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "fake image data".getBytes()
        );

        PostDto dto = new PostDto(
                1,
                "Title",
                "Content",
                "test.png",
                LocalDateTime.now(),
                null,
                1,
                1
        );

        Mockito.when(postService.savePost(anyString(), anyInt(), anyString(), anyString(), any()))
                .thenReturn(dto);

        mockMvc.perform(multipart("/blogers/author/post/create")
                        .file(image)
                        .param("title", "Title")
                        .param("content", "Content")
                        .param("categoryId", "1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    // ==========================================
    // UPDATE POST
    // ==========================================
    @WithMockUser(username = "author@gmail.com", roles = {"AUTHOR"})
    @Test
    void testUpdatePost() throws Exception {

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "",
                MediaType.TEXT_PLAIN_VALUE,
                "".getBytes() // empty optional file
        );

        PostDto dto = new PostDto(
                1,
                "Updated",
                "Updated content",
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1,
                1
        );

        Mockito.when(postService.updatePostService(anyString(), anyInt(), anyString(), anyString(), any(), anyInt()))
                .thenReturn(dto);

        mockMvc.perform(multipart("/blogers/author/post/update/1")
                        .file(image)
                        .param("title", "Updated")
                        .param("content", "Updated content")
                        .param("categoryId", "1")
                        .with(request -> { request.setMethod("PUT"); return request; }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    // ==========================================
    // GET ALL POSTS
    // ==========================================
    @WithMockUser(roles = {"AUTHOR"})
    @Test
    void testGetAllPosts() throws Exception {

        List<PostDto> list = List.of(
                new PostDto(1, "A", "Content A", null, null, null, 1, 1),
                new PostDto(2, "B", "Content B", null, null, null, 1, 1)
        );

        Mockito.when(postService.getAllPost()).thenReturn(list);

        mockMvc.perform(get("/blogers/author/post/get-all"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].title").value("A"));
    }

    // ==========================================
    // GET POST BY ID
    // ==========================================
    @WithMockUser(roles = {"AUTHOR"})
    @Test
    void testGetByIdPost() throws Exception {

        PostDto dto = new PostDto(1, "Test", "Content", null, null, null, 1, 1);

        Mockito.when(postService.getPostById(1)).thenReturn(dto);

        mockMvc.perform(get("/blogers/author/post/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test"));
    }

    // ==========================================
    // DELETE POST
    // ==========================================
    @WithMockUser(username = "author@gmail.com", roles = {"AUTHOR"})
    @Test
    void testDeleteByIdPost() throws Exception {

        Mockito.when(postService.deletePostById(1, "author@gmail.com"))
                .thenReturn(true);

        mockMvc.perform(delete("/blogers/author/post/delete-by-id/1"))
                .andExpect(status().isNoContent());
    }
}
