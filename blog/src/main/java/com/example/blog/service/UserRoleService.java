package com.example.blog.service;

import com.example.blog.dto.UserRoleDto;
import com.example.blog.entity.UserRole;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.UserRoleRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.blog.constants.ErrorMessages.ROLE_NOT_FOUND;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private ModelMapper modelMapper;
    //create user role
    @Transactional
    @CacheEvict(value = "roles", allEntries = true)
    public UserRoleDto createUserRole(UserRoleDto userRoleDto){

        UserRole userRole = modelMapper.map(userRoleDto,UserRole.class);
        UserRole savedUserRole = userRoleRepository.save(userRole);
        return modelMapper.map(savedUserRole,UserRoleDto.class);
    }
    //update user role
    @Transactional
    @CachePut(value = "role",key = "#id")
    @CacheEvict(value = "roles",allEntries = true)
    public UserRoleDto updateUserRole(Integer id,UserRoleDto updateRoleDto){
        UserRole exitUserRole = userRoleRepository.findById(id)
                .orElseThrow( () ->new ResourceNotFoundException(ROLE_NOT_FOUND));
        exitUserRole.setUserRoleName(updateRoleDto.getUserRoleName());
        UserRole updateRole = userRoleRepository.save(exitUserRole);
        return modelMapper.map(updateRole,UserRoleDto.class);
    }
    //get all
    @Transactional(readOnly = true)
    @Cacheable("roles")
    public List<UserRoleDto> findAllUserRoles(){
        return userRoleRepository.findAll()
                .stream()
                .map(userRole -> modelMapper.map(userRole,UserRoleDto.class))
                .toList();
    }
    //get by id
    @Cacheable(value = "role", key = "#id")
    public UserRoleDto getByIdUserRoles(Integer id){
        UserRole exitUser = userRoleRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException(ROLE_NOT_FOUND));
        return modelMapper.map( exitUser,UserRoleDto.class);
    }
    //delete by id
    @Transactional
    @CacheEvict(value = {"role","roles"},allEntries = true)
    public void deleteByIdUserRoles(Integer id){
        if(!userRoleRepository.existsById(id)) {
                throw  new ResourceNotFoundException("ROLE_NOT_FOUND");
        }
         userRoleRepository.deleteById(id);
    }
}
