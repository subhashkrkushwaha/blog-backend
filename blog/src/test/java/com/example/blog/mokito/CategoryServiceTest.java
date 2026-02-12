package com.example.blog.mokito;


import com.example.blog.dto.CategoryDto;
import com.example.blog.entity.Category;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.service.CategoryService;
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
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryService categoryService;

    // -------------------------------------------------------------
    // createCategory
    // -------------------------------------------------------------
    @Test
    void testCreateCategory() {
        CategoryDto dto = new CategoryDto(null, "Tech");
        Category category = new Category(1, "Tech");

        when(modelMapper.map(dto, Category.class)).thenReturn(category);
        when(modelMapper.map(category, CategoryDto.class))
                .thenReturn(new CategoryDto(1, "Tech"));

        CategoryDto result = categoryService.createCategory(dto);

        assertNotNull(result);
        assertEquals("Tech", result.getCategory());
        verify(categoryRepository, times(1)).save(category);
    }

    // -------------------------------------------------------------
    // updateCategory
    // -------------------------------------------------------------
    @Test
    void testUpdateCategory_success() {
        Category exists = new Category(1, "Old");
        CategoryDto updateDto = new CategoryDto(1, "New");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(exists));
        when(categoryRepository.save(exists)).thenReturn(exists);
        when(modelMapper.map(exists, CategoryDto.class))
                .thenReturn(new CategoryDto(1, "New"));

        CategoryDto result = categoryService.updateCategory(1, updateDto);

        assertEquals("New", result.getCategory());
        verify(categoryRepository).save(exists);
    }

    @Test
    void testUpdateCategory_notFound() {
        CategoryDto updateDto = new CategoryDto(1, "New");

        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateCategory(1, updateDto));
    }

    // -------------------------------------------------------------
    // getAllCategory
    // -------------------------------------------------------------
    @Test
    void testGetAllCategory() {
        Category category = new Category(1, "Tech");

        when(categoryRepository.findAll()).thenReturn(List.of(category));
        when(modelMapper.map(category, CategoryDto.class))
                .thenReturn(new CategoryDto(1, "Tech"));

        List<CategoryDto> result = categoryService.getAllCategory();

        assertEquals(1, result.size());
        assertEquals("Tech", result.get(0).getCategory());
    }

    // -------------------------------------------------------------
    // getByIdCategory
    // -------------------------------------------------------------
    @Test
    void testGetByIdCategory_success() {
        Category category = new Category(1, "Tech");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(modelMapper.map(category, CategoryDto.class))
                .thenReturn(new CategoryDto(1, "Tech"));

        CategoryDto result = categoryService.getByIdCategory(1);

        assertNotNull(result);
        assertEquals("Tech", result.getCategory());
    }

    @Test
    void testGetByIdCategory_notFound() {
        when(categoryRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getByIdCategory(1));
    }

    // -------------------------------------------------------------
    // deleteByIdCategory
    // -------------------------------------------------------------
    @Test
    void testDeleteByIdCategory_success() {
        when(categoryRepository.existsById(1)).thenReturn(true);

        categoryService.deleteByIdCategory(1);

        verify(categoryRepository).deleteById(1);
    }

    @Test
    void testDeleteByIdCategory_notFound() {
        when(categoryRepository.existsById(1)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> categoryService.deleteByIdCategory(1));
    }
}
