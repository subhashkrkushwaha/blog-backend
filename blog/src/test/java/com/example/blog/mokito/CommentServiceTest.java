package com.example.blog.mokito;


import com.example.blog.dto.CommentDto;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentService commentService;

    // -------------------------------------------------------------
    // saveComment
    // -------------------------------------------------------------
    @Test
    void testSaveComment_success() {
        CommentDto dto = new CommentDto(null, "Nice post!", null, 10);

        User user = new User();
        user.setId(1);

        Post post = new Post();
        post.setId(10);

        Comment savedComment = new Comment();
        savedComment.setId(5);
        savedComment.setComment("Nice post!");
        savedComment.setUser(user);
        savedComment.setPost(post);

        when(userRepository.findByUserEmail("abc@gmail.com"))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(10))
                .thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(savedComment);
        when(modelMapper.map(savedComment, CommentDto.class))
                .thenReturn(new CommentDto(5, "Nice post!", 1, 10));

        CommentDto result = commentService.saveComment(dto, "abc@gmail.com");

        assertNotNull(result);
        assertEquals("Nice post!", result.getComment());
        assertEquals(1, result.getUserId());
        assertEquals(10, result.getPostId());
    }

    @Test
    void testSaveComment_userNotFound() {
        CommentDto dto = new CommentDto(null, "Hello", null, 10);

        when(userRepository.findByUserEmail("abc@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.saveComment(dto, "abc@gmail.com"));
    }

    @Test
    void testSaveComment_postNotFound() {
        CommentDto dto = new CommentDto(null, "Hello", null, 10);

        User user = new User();
        user.setId(1);

        when(userRepository.findByUserEmail("abc@gmail.com"))
                .thenReturn(Optional.of(user));
        when(postRepository.findById(10))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.saveComment(dto, "abc@gmail.com"));
    }

    // -------------------------------------------------------------
    // getAllComment
    // -------------------------------------------------------------
    @Test
    void testGetAllComment() {
        Comment c = new Comment();
        c.setId(1);
        c.setComment("Great!");

        when(commentRepository.findAll()).thenReturn(List.of(c));
        when(modelMapper.map(c, CommentDto.class))
                .thenReturn(new CommentDto(1, "Great!", 1, 10));

        List<CommentDto> result = commentService.getAllComment();

        assertEquals(1, result.size());
        assertEquals("Great!", result.get(0).getComment());
    }

    // -------------------------------------------------------------
    // getByIdComment
    // -------------------------------------------------------------
    @Test
    void testGetByIdComment_success() {
        Comment c = new Comment();
        c.setId(1);
        c.setComment("Good");

        when(commentRepository.findById(1))
                .thenReturn(Optional.of(c));
        when(modelMapper.map(c, CommentDto.class))
                .thenReturn(new CommentDto(1, "Good", 1, 10));

        CommentDto result = commentService.getByIdComment(1);

        assertNotNull(result);
        assertEquals("Good", result.getComment());
    }

    @Test
    void testGetByIdComment_notFound() {
        when(commentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.getByIdComment(1));
    }

    // -------------------------------------------------------------
    // deleteByIdComment
    // -------------------------------------------------------------
    @Test
    void testDeleteByIdComment_success() {
        when(commentRepository.existsById(1)).thenReturn(true);

        commentService.deleteByIdComment(1);

        verify(commentRepository).deleteById(1);
    }

    @Test
    void testDeleteByIdComment_notFound() {
        when(commentRepository.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> commentService.deleteByIdComment(1));
    }
}
