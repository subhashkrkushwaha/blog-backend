package com.example.blog.mokito;

import com.example.blog.dto.UserRoleDto;
import com.example.blog.entity.UserRole;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.UserRoleRepository;
import com.example.blog.service.UserRoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserRoleServiceTestM {

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserRoleService userRoleService;

    // ------------------------------------------
    // 🔥 TEST: createUserRole()
    // ------------------------------------------
    @Test
    void testCreateUserRole() {
        UserRoleDto dto = new UserRoleDto(null, "ADMIN");
        UserRole savedEntity = new UserRole();
        savedEntity.setId(1);
        savedEntity.setUserRoleName("ADMIN");

        when(modelMapper.map(dto, UserRole.class)).thenReturn(savedEntity);
        when(userRoleRepository.save(savedEntity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, UserRoleDto.class))
                .thenReturn(new UserRoleDto(1, "ADMIN"));

        UserRoleDto result = userRoleService.createUserRole(dto);

        assertNotNull(result);
        assertEquals("ADMIN", result.getUserRoleName());
        verify(userRoleRepository, times(1)).save(savedEntity);
    }

    // ------------------------------------------
    // 🔥 TEST: updateUserRole()
    // ------------------------------------------
    @Test
    void testUpdateUserRole() {
        Integer id = 1;
        UserRoleDto dto = new UserRoleDto(null, "SUPER_ADMIN");

        UserRole existing = new UserRole();
        existing.setId(1);
        existing.setUserRoleName("ADMIN");

        UserRole updated = new UserRole();
        updated.setId(1);
        updated.setUserRoleName("SUPER_ADMIN");

        when(userRoleRepository.findById(id)).thenReturn(Optional.of(existing));
        when(userRoleRepository.save(existing)).thenReturn(updated);
        when(modelMapper.map(updated, UserRoleDto.class))
                .thenReturn(new UserRoleDto(1, "SUPER_ADMIN"));

        UserRoleDto result = userRoleService.updateUserRole(1, dto);

        assertNotNull(result);
        assertEquals("SUPER_ADMIN", result.getUserRoleName());
    }

    @Test
    void testUpdateUserRole_NotFound() {
        when(userRoleRepository.findById(10)).thenReturn(Optional.empty());

        UserRoleDto dto = new UserRoleDto(null, "ADMIN");

        assertThrows(ResourceNotFoundException.class,
                () -> userRoleService.updateUserRole(10, dto));
    }

    // ------------------------------------------
    // 🔥 TEST: findAllUserRoles()
    // ------------------------------------------
    @Test
    void testFindAllUserRoles() {
        UserRole role = new UserRole();
        role.setId(1);
        role.setUserRoleName("USER");

        when(userRoleRepository.findAll()).thenReturn(List.of(role));
        when(modelMapper.map(any(), eq(UserRoleDto.class)))
                .thenReturn(new UserRoleDto(1, "USER"));

        List<UserRoleDto> list = userRoleService.findAllUserRoles();

        assertEquals(1, list.size());
        assertEquals("USER", list.getFirst().getUserRoleName());
    }

    // ------------------------------------------
    // 🔥 TEST: getByIdUserRoles()
    // ------------------------------------------
    @Test
    void testGetByIdUserRoles() {
        UserRole role = new UserRole();
        role.setId(1);
        role.setUserRoleName("MANAGER");

        when(userRoleRepository.findById(1)).thenReturn(Optional.of(role));
        when(modelMapper.map(role, UserRoleDto.class))
                .thenReturn(new UserRoleDto(1, "MANAGER"));

        UserRoleDto result = userRoleService.getByIdUserRoles(1);

        assertNotNull(result);
        assertEquals("MANAGER", result.getUserRoleName());
    }

    @Test
    void testGetByIdUserRoles_NotFound() {
        when(userRoleRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userRoleService.getByIdUserRoles(99));
    }

    // ------------------------------------------
    // 🔥 TEST: deleteByIdUserRoles()
    // ------------------------------------------
    @Test
    void testDeleteByIdUserRoles() {
        when(userRoleRepository.existsById(1)).thenReturn(true);
        doNothing().when(userRoleRepository).deleteById(1);

        assertDoesNotThrow(() -> userRoleService.deleteByIdUserRoles(1));
    }

    @Test
    void testDeleteByIdUserRoles_NotFound() {
        when(userRoleRepository.existsById(99)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> userRoleService.deleteByIdUserRoles(99));
    }
}
