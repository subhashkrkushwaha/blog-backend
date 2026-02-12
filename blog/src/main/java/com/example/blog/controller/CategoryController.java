package com.example.blog.controller;

import com.example.blog.dto.CategoryDto;
import com.example.blog.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Category API")
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/ADMIN/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    //create
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto category  = categoryService.createCategory(categoryDto);
        return  ResponseEntity.status(HttpStatus.CREATED).body(category);
    }
    //update
    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Integer id,@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.status(HttpStatus.OK).body( categoryService.updateCategory(id,categoryDto));
    }
    //get all
    @GetMapping("/get-all")
    public ResponseEntity<List<CategoryDto>> getAllUser(){
        return ResponseEntity.accepted().body(categoryService.getAllCategory());
    }
    // get by id
    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<CategoryDto> getByIdCategory(@PathVariable Integer id){
        return  ResponseEntity.ok().body( categoryService.getByIdCategory(id));
    }
    //delete by id
    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<Void> deleteByIdCategory(@PathVariable Integer id) {
        categoryService.deleteByIdCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
