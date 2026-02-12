package com.example.blog.controller;

import com.example.blog.dto.CommentDto;
import com.example.blog.entity.Comment;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CommentRepository;
import com.example.blog.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Comment API")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
@RequestMapping("/USER/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentRepository commentRepository;

    //create
    @PostMapping("/create")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        CommentDto cDto =  commentService.saveComment(commentDto,email);
        return  ResponseEntity.status(HttpStatus.CREATED).body(cDto);
    }

    //get all
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<List<CommentDto>> getAllComment(){
        return ResponseEntity.accepted().body(commentService.getAllComment());
    }
    // get by id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<CommentDto> getByIdComment(@PathVariable Integer id){
        return  ResponseEntity.ok().body( commentService.getByIdComment(id));
    }
    //delete by id
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteByIdComment(@PathVariable Integer id) {
        commentService.deleteByIdComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //delete own comment
    public void deleteOwnComment(Integer id, String email) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getUser().getUserEmail().equals(email)) {
            throw new ResourceNotFoundException("You are not allowed to delete this comment");
        }
        commentRepository.delete(comment);
    }

}
