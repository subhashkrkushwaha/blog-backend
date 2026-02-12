package com.example.blog.service;


import com.example.blog.dto.CategoryDto;
import com.example.blog.entity.Category;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.blog.constants.ErrorMessages.CATEGORY_NOT_FOUND;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;
    // create category

    @Transactional
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        categoryRepository.save(category);
        return modelMapper.map(category, CategoryDto.class);
    }
    //update
    @Transactional
    @CachePut(value = "category", key = "#id")
    @CacheEvict(value = "categories", allEntries = true)
    public CategoryDto updateCategory(Integer id,CategoryDto categoryDto){
        Category existsCategory = categoryRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException(CATEGORY_NOT_FOUND));
        existsCategory.setCategory(categoryDto.getCategory());
        Category updateCategory=categoryRepository.save(existsCategory);
        return modelMapper.map(updateCategory, CategoryDto.class);
    }
    // get all
    @Cacheable(value = "categories")
    public List<CategoryDto> getAllCategory(){
        return categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category,CategoryDto.class))
                .toList();
    }
    //get by id
    @Cacheable(value = "category",key = "#id")
    public CategoryDto getByIdCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException(CATEGORY_NOT_FOUND));
        return modelMapper.map(category, CategoryDto.class);
    }
    // delete by id
    @Transactional
    @CacheEvict(value = {"category","categories"}, allEntries = true)
    public void deleteByIdCategory(Integer id) {
        if(!categoryRepository.existsById(id)){
            throw new ResourceNotFoundException(CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(id);
    }
}
