package com.example.blog.mokito;

//package com.example.blog.service;

import com.cloudinary.Cloudinary;
import com.example.blog.dto.PostDto;
import com.example.blog.entity.Category;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    MultipartFile file;

    @InjectMocks
    private PostService postService;

    User user;
    Category category;
    Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1);

        category = new Category();
        category.setId(1);

        post = new Post();
        post.setId(1);
        post.setTitle("Test Title");
        post.setContent("Test Content");
    }

    // ------------------------------------------------------------
    // SAVE POST
    // ------------------------------------------------------------
    @Test
    void testSavePost_success() throws Exception {
        when(userRepository.findByUserEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findById(1))
                .thenReturn(Optional.of(category));

        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(file.getBytes()).thenReturn("abc".getBytes());

        when(postRepository.save(any(Post.class))).thenReturn(post);

        PostDto dto = new PostDto();
        when(modelMapper.map(any(Post.class), eq(PostDto.class))).thenReturn(dto);

        PostDto result = postService.savePost("test@gmail.com", 1, "New Title", "New Content", file);
        assertNotNull(result);

        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testSavePost_userNotFound() {
        when(userRepository.findByUserEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.savePost("unknown@gmail.com", 1, "A", "B", file);
        });
    }

    @Test
    void testSavePost_categoryNotFound() {
        when(userRepository.findByUserEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.savePost("test@gmail.com", 1, "A", "B", file);
        });
    }

    // ------------------------------------------------------------
    // UPDATE POST
    // ------------------------------------------------------------
    @Test
    void testUpdatePost_success() throws Exception {
        when(postRepository.findById(1)).thenReturn(Optional.of(post));
        when(userRepository.findByUserEmail("test@gmail.com"))
                .thenReturn(Optional.of(user));
        when(categoryRepository.findById(1))
                .thenReturn(Optional.of(category));

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("newImage.jpg");
        when(file.getBytes()).thenReturn("bytes".getBytes());

        // Mock cloudinary upload
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("secure_url", "http://image.com/newImage.jpg");
        when(cloudinary.uploader().upload(any(), any())).thenReturn(uploadResult);

        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(modelMapper.map(any(Post.class), eq(PostDto.class)))
                .thenReturn(new PostDto());

        PostDto result = postService.updatePostService(
                "test@gmail.com", 1, "Updated", "Updated", file, 1
        );

        assertNotNull(result);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void testUpdatePost_postNotFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.updatePostService(
                    "test@gmail.com", 1, "A", "B", file, 1
            );
        });
    }

    // ------------------------------------------------------------
    // GET ALL POSTS
    // ------------------------------------------------------------
    @Test
    void testGetAllPost() {
        when(postRepository.findAll()).thenReturn(List.of(post));
        when(modelMapper.map(any(Post.class), eq(PostDto.class)))
                .thenReturn(new PostDto());

        List<PostDto> list = postService.getAllPost();

        assertEquals(1, list.size());
    }

    // ------------------------------------------------------------
    // GET POST BY ID
    // ------------------------------------------------------------
    @Test
    void testGetPostById_success() {
        when(postRepository.findById(1))
                .thenReturn(Optional.of(post));

        when(modelMapper.map(any(Post.class), eq(PostDto.class)))
                .thenReturn(new PostDto());

        PostDto dto = postService.getPostById(1);
        assertNotNull(dto);
    }

    @Test
    void testGetPostById_notFound() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> postService.getPostById(1));
    }

    // ------------------------------------------------------------
    // DELETE POST BY ID
    // ------------------------------------------------------------
    @Test
    void testDeletePost_success() {
        when(userService.loadByEmail("test@gmail.com"))
                .thenReturn(user);

        Post p = new Post();
        p.setId(1);

        user.setPosts(new ArrayList<>(List.of(p))); // ✅ FIXED

        boolean result = postService.deletePostById(1, "test@gmail.com");

        assertTrue(result);
        verify(postRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeletePost_postNotOwned() {
        when(userService.loadByEmail("test@gmail.com"))
                .thenReturn(user);

        user.setPosts(new ArrayList<>()); // FIXED

        boolean result = postService.deletePostById(1, "test@gmail.com");

        assertFalse(result);
        verify(postRepository, never()).deleteById(any());
    }
}
