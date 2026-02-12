package com.example.blog.controller;

import com.example.blog.dto.UserDto;
import com.example.blog.service.UserService;
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
@Tag(name = "User API")
@RequestMapping("/USER")
public class UserController {

    @Autowired
    private UserService userService;
    //create
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto userDto1 = userService.createUser(userDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(userDto1);
    }
    //update
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.status(HttpStatus.OK).body( userService.updateUser(userDto,email));
    }
    //get all
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<List<UserDto>> getAllUser(){
        return ResponseEntity.accepted().body(userService.getAllUser());
    }
    // get by id
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<UserDto> getByIdUser(@PathVariable Integer id){
        return  ResponseEntity.ok().body( userService.getByIdUser(id));
    }
    //delete by id throw ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteByIdUser(@PathVariable Integer id) {
                            userService.deleteByIdUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    //delete by OWN Account by user
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/own-account-delete")
    public ResponseEntity<Void> ownAccountDelete() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
                            userService.ownAccountDelete(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
