package com.example.blog.service;

import com.example.blog.dto.CommentDto;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.blog.constants.ErrorMessages.*;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    // create comment
    @Transactional
    public CommentDto saveComment(CommentDto commentDto,String email){
        // fetch user
        User user = userRepository.findByUserEmail(email
                )
                .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
        Comment comment = new Comment();
        comment.setComment(commentDto.getComment());
        // fetch post
        Post post = postRepository.findById(commentDto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(POST_NOT_FOUND));
        //comment.setUser(userRepository.getById(user.getId()));
        //comment.setPost(postRepository.getById(post.getId()));
        comment.setUser(user);
        comment.setPost(post);
        Comment saved = commentRepository.save(comment);
        return modelMapper.map(saved, CommentDto.class);
    }
    //get all
    public List<CommentDto> getAllComment(){
        return commentRepository.findAll()
                .stream()
                .map(comment -> modelMapper.map(comment,CommentDto.class))
                .toList();
    }
    //get by id
    public CommentDto getByIdComment(Integer id){

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(COMMENT_NOT_FOUND));
        return modelMapper.map(comment,CommentDto.class);
    }

    // delete by id
    @Transactional
    public void deleteByIdComment(Integer id){
       if(!commentRepository.existsById(id)){
           throw new ResourceNotFoundException(COMMENT_NOT_FOUND);
       }
        commentRepository.deleteById(id);
    }
}
