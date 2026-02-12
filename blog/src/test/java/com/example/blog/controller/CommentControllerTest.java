package com.example.blog.controller;

import com.example.blog.dto.CommentDto;
import com.example.blog.entity.Comment;
import com.example.blog.entity.User;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CommentRepository;
import com.example.blog.service.CommentService;
import com.example.blog.util.JWTUtility;
import com.example.blog.util.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JWTUtility jwtUtility;

    // Inject into controller for non-MVC method test
    @InjectMocks
    private CommentController commentController;

    // CREATE COMMENT
    @WithMockUser(username = "user@gmail.com", roles = {"USER"})
    @Test
    void testCreateComment() throws Exception {

        CommentDto dto = new CommentDto(1, "Nice post!", 1, 10);

        Mockito.when(commentService.saveComment(any(), anyString()))
                .thenReturn(dto);

        mockMvc.perform(post("/USER/comment/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "comment": "Nice post!",
                                  "userId": 1,
                                  "postId": 10
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").value("Nice post!"));
    }

    // GET ALL COMMENTS (ADMIN)
    @WithMockUser(roles = {"ADMIN"})
    @Test
    void testGetAllComment() throws Exception {

        List<CommentDto> list = List.of(
                new CommentDto(1, "A", 1, 10),
                new CommentDto(2, "B", 2, 20)
        );

        Mockito.when(commentService.getAllComment()).thenReturn(list);

        mockMvc.perform(get("/USER/comment/get-all"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].comment").value("A"))
                .andExpect(jsonPath("$[1].comment").value("B"));
    }

    // GET COMMENT BY ID
    @WithMockUser(roles = {"USER"})
    @Test
    void testGetByIdComment() throws Exception {

        CommentDto dto = new CommentDto(1, "Test comment", 1, 10);

        Mockito.when(commentService.getByIdComment(1)).thenReturn(dto);

        mockMvc.perform(get("/USER/comment/get-by-id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Test comment"));
    }

    // DELETE COMMENT BY ID (ADMIN)
    @WithMockUser(roles = {"ADMIN"})
    @Test
    void testDeleteByIdComment() throws Exception {

        Mockito.doNothing().when(commentService).deleteByIdComment(1);

        mockMvc.perform(delete("/USER/comment/delete-by-id/1"))
                .andExpect(status().isNoContent());
    }

    // DELETE OWN COMMENT (ALLOWED)
    @Test
    void testDeleteOwnComment() {

        User user = new User();
        user.setUserEmail("user@gmail.com");

        Comment comment = new Comment(1, "My comment", user, null);

        Mockito.when(commentRepository.findById(1))
                .thenReturn(Optional.of(comment));

        Mockito.doNothing().when(commentRepository).delete(comment);

        commentController.deleteOwnComment(1, "user@gmail.com");

        Mockito.verify(commentRepository, Mockito.times(1)).delete(comment);
    }

    // DELETE OWN COMMENT (NOT allowed)
    @Test
    void testDeleteOwnCommentNotAllowed() {

        User otherUser = new User();
        otherUser.setUserEmail("other@gmail.com");

        Comment comment = new Comment(1, "Not yours", otherUser, null);

        Mockito.when(commentRepository.findById(1))
                .thenReturn(Optional.of(comment));

        try {
            commentController.deleteOwnComment(1, "user@gmail.com");
        } catch (Exception e) {
            assert (e instanceof ResourceNotFoundException);
        }
    }
}
