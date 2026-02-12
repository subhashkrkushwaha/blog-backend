package com.example.blog.controller;

import com.example.blog.dto.PostDto;
import com.example.blog.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "Post API", description = "createPost,updatePost,getAll,getById,deleteById")
@PreAuthorize("hasRole('AUTHOR')")
@RequestMapping("/blogers/author/post")
public class PostController {

    @Autowired
    private PostService postService;

    public static final String blogImage = "C:\\Users\\santa\\Downloads\\blog\\blogImage";
    //create
    @PostMapping(value = "/create")
    public ResponseEntity<PostDto> createPost(
                                     @RequestParam String title,
                                     @RequestParam String content,
                                     @RequestParam("image") MultipartFile image,
                                     @RequestParam(required = false) Integer userId,
                                     @RequestParam Integer categoryId  )  throws Exception{
/*         String fileName = image.getOriginalFilename();//get image name like fitness.png
           Path path = Paths.get(blogImage, fileName);// set path where to save data like C:/uploads/blog/photo.png
           Files.createDirectories(path.getParent());// if folder exit or If C:/uploads/blog doesn’t exist, Java creates it automatically
           Files.write(path, image.getBytes());the uploaded image to disk Reads bytes from MultipartFile Saves  file on your system*/
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          String email = authentication.getName();
           PostDto createpostDto = postService.savePost( email,  categoryId,title,content, image);
           return  ResponseEntity.status(HttpStatus.CREATED).body(createpostDto);
    }
    //update
    @PutMapping("/update/{id}")
    public ResponseEntity<PostDto> updatePost(
                                              @PathVariable Integer id,
                                              @RequestParam String title,
                                              @RequestParam String content,
                                              @RequestParam(required = false) MultipartFile image,
                                              /*@RequestParam Integer userId,*/
                                              @RequestParam Integer categoryId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        PostDto dto = postService.updatePostService(email,id, title,content, image,  categoryId);
        return  ResponseEntity.status(HttpStatus.OK).body(dto);
    }
    //get all user/reader
    @GetMapping("/get-all")
    public ResponseEntity<List<PostDto>> getAllP(){
        return ResponseEntity.accepted().body(postService.getAllPost());
    }
    // get by id admin
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<PostDto> getByIdP(@PathVariable Integer id){
        return  ResponseEntity.ok().body( postService.getPostById(id));
    }
    //delete by id
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<String> deleteByIdP(@PathVariable Integer id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            boolean delete =  postService.deletePostById(id,email);
            if(delete){
                return new ResponseEntity<>("Journal deleted successfully",HttpStatus.NO_CONTENT);
            }
            return  new  ResponseEntity<>("Journal not deleted successfully",HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
