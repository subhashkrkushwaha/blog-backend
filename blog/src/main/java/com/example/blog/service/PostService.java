package com.example.blog.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.blog.dto.PostDto;
import com.example.blog.entity.Category;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.blog.constants.ErrorMessages.*;

@Service
public class  PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private Cloudinary cloudinary;
    public static final String blogImage = "C:\\Users\\santa\\Downloads\\blog\\blogImage";

    //create new post
    @Transactional
    @CacheEvict(value = "posts", allEntries = true)
    public PostDto savePost(String email,Integer categoryId,String title,String content,MultipartFile image) throws Exception{
        try{

            // 1️⃣ Fetch User
            User user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
            // 2️⃣ Fetch Category
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND));
            //local database save image
            String fileName = image.getOriginalFilename();//get Original FileName
            Path path = Paths.get(blogImage,fileName);//set file path
            Files.createDirectories(path.getParent());// if folder is exits or create new folder not give error
            Files.write(path,image.getBytes());
            //save image in cloudinary
/*        Map<?, ?> uploadResult = cloudinary.uploader().upload(
                image.getBytes(),
                ObjectUtils.asMap("folder", "blog-images"));
        String imageUrl = uploadResult.get("secure_url").toString();*/
            Post post = new Post();
            post.setTitle(title);
            post.setContent(content);
            post.setImage(fileName);
//        post.setImage(imageUrl);
            post.setUser(userRepository.getById(user.getId()));
            post.setCategory(categoryRepository.getById(categoryId));
            Post savePost =  postRepository.save(post);
            return modelMapper.map(savePost,PostDto.class);
        }catch (Exception e){
            throw new RuntimeException("Any error during saving new  Post"+e.getMessage());
        }
    }
    //  Upload to CLOUDINARY
/*    public String uploadCloudinary(MultipartFile image) throws Exception {
        Map<?, ?> result = cloudinary.uploader().upload(
                image.getBytes(),
                ObjectUtils.asMap("folder", "blog-images")
        );
        return result.get("secure_url").toString(); // store in DB
    }*/
    //update
    @Transactional
    @CachePut(value = "post",key = "#postId")
    @CacheEvict(value = "posts",allEntries = true)
    public PostDto updatePostService(String email,Integer postId,String title, String content,
                                     MultipartFile image, Integer categoryId) throws Exception{
        try{
            // Fetch Post
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND));
            // 1️⃣ Fetch User
            User user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
            // 2️⃣ Fetch Category
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(CATEGORY_NOT_FOUND));
            post.setTitle(title);
            post.setUpdateTime(LocalDateTime.now());
            post.setContent(content);
            String fileName = null;
            if (image != null && !image.isEmpty()) {
                fileName = image.getOriginalFilename();
                Path uploadDir = Paths.get(blogImage);
                Files.createDirectories(uploadDir);
                Path filePath = uploadDir.resolve(fileName);
                Files.write(filePath, image.getBytes());
            }
            if (image != null) {
                Map<?, ?> uploadResult = cloudinary.uploader().upload(
                        image.getBytes(),
                        ObjectUtils.asMap("folder", "blog-images"));
                String imageUrl = uploadResult.get("secure_url").toString();
                post.setImage(imageUrl);//server
//            post.setImage(fileName);//local database
            }
            post.setUser(userRepository.getById(user.getId()));
            post.setCategory(categoryRepository.getById(categoryId));
            Post savePost =  postRepository.save(post);
            return modelMapper.map(savePost,PostDto.class);
        }catch (Exception e){
            throw  new ResourceNotFoundException("Any error during updating  post"+e.getMessage());
        }
    }
    // get all post
    @Cacheable(value = "posts")
    public List<PostDto> getAllPost(){
        return postRepository.findAll()
                .stream()
                .map(post -> modelMapper.map(post, PostDto.class))
                .toList();
    }
    //get by id
    @Cacheable(value = "post",key = "#id")
    public PostDto getPostById(Integer id){
        Post post = postRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException(POST_NOT_FOUND+id));
        return modelMapper.map(post,PostDto.class);
    }

    @Transactional
    @CacheEvict(value = {"post", "posts"}, allEntries = true)
    public Boolean deletePostById(Integer id,String email){
        boolean remove = false;
        try {
            User user = userService.loadByEmail(email);
            remove = user.getPosts().removeIf(post -> post.getId().equals(id));

            if(remove){
                postRepository.deleteById(id);
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("An error occurred while detecting the entry."+e.getMessage());
        }
        return remove;
    }

    //delete by id
}
