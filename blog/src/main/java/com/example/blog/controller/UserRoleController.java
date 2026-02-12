package com.example.blog.controller;

import com.example.blog.dto.UserRoleDto;
import com.example.blog.service.UserRoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "User Role API")
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/ADMIN")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;
    //Create new user role
    @PostMapping("/create-role")
    public ResponseEntity<UserRoleDto> createNewUserRole(@RequestBody UserRoleDto userRoleDto){
        UserRoleDto userCreate= userRoleService.createUserRole(userRoleDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreate);
    }
    //Update user role
    @PutMapping("/update-role/{id}")
    public ResponseEntity<UserRoleDto> updateNewUserRole(@PathVariable Integer id, @RequestBody UserRoleDto userRoleDto){
        UserRoleDto roleUpdate= userRoleService.updateUserRole(id,userRoleDto);
        return ResponseEntity.ok(roleUpdate);
    }
    //get all user role
    @GetMapping("/getAll-role")
    public ResponseEntity<List<UserRoleDto>> getAllRole(){
        return ResponseEntity.ok( userRoleService.findAllUserRoles());
    }
    //Get By id user role
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<UserRoleDto> getByIdUserRole(@PathVariable Integer id){
       return ResponseEntity.ok( userRoleService.getByIdUserRoles(id));
    }
    //Delete user role
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteByIdUserRole(@PathVariable Integer id){
          userRoleService.deleteByIdUserRoles(id);
        return ResponseEntity.noContent().build();
    }
}
