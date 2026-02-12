package com.example.blog.service;

import com.example.blog.dto.UserDto;
import com.example.blog.entity.User;
import com.example.blog.entity.UserRole;
import com.example.blog.exception.ResourceNotFoundException;
import com.example.blog.repository.UserRepository;
import com.example.blog.repository.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.blog.constants.ErrorMessages.ROLE_NOT_FOUND;
import static com.example.blog.constants.ErrorMessages.USER_NOT_FOUND;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    // create new account
    @Transactional
    public UserDto createUser( UserDto userDto){
        try {
            User user = modelMapper.map(userDto, User.class);
            user.setCurrentTime(LocalDateTime.now());
            List<UserRole> roles = userDto.getUserRoles()
                    .stream()
                    .map(userRoleDto -> userRoleRepository.findById(userRoleDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND + userRoleDto.getId())))
                    .toList();//Immutable = cannot be changed after creation
            user.setUserRoles(roles);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            User saveUser = userRepository.save(user);
            return modelMapper.map(saveUser, UserDto.class);
        }catch (Exception e){
            log.error("@Slf4f : ",e.getMessage());
            return userDto;
        }
    }
    // UPDATE
    @Transactional
    @CachePut(value = "userById", key = "#result.id")
    @CacheEvict(value = "allUsers", allEntries = true)
    public UserDto updateUser( UserDto userDto,String email){
        try {
            User user = userRepository.findByUserEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
            if (userDto.getUserName() != null) {
                user.setUserName(userDto.getUserName());
            }
            if (userDto.getUserEmail() != null) {
                user.setUserEmail(userDto.getUserEmail());
            }
            if (userDto.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            if (userDto.getUserRoles() != null) {
                List<UserRole> roles = userDto.getUserRoles()
                        .stream()
                        .map(userRoleDto -> userRoleRepository.findById(userRoleDto.getId())
                                        .orElseThrow(() ->
                                                new ResourceNotFoundException(ROLE_NOT_FOUND + userDto.getId())))
//                        .toList();//Immutable = cannot be changed after creation
                        .collect(Collectors.toList()); // mutable
                user.setUserRoles(roles);
            }
            User updateUser = userRepository.save(user);
            return modelMapper.map(updateUser, UserDto.class);
        }catch (Exception e){
            log.error("@Slf4f : ",e.getMessage());
        }
        return userDto;
    }
    // GET ALL
    @Cacheable("users")
    public List<UserDto> getAllUser(){
        return  userRepository.findAll()
                .stream()
                .map(   user -> modelMapper.map(user,UserDto.class))
                .toList();
    }
    // find by email
     public User loadByEmail(String userEmail){
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(()->  new UsernameNotFoundException("User not found with email: " + userEmail));
     }
    // GET BY ID
    @Cacheable(value = "user",key = "#id")
    public UserDto getByIdUser( Integer id){
       User user = userRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
       return modelMapper.map(user,UserDto.class);
    }
    // DELETE BY ID
    @Transactional
    @CacheEvict(value = {"userById", "allUsers"}, allEntries = true)
    public void deleteByIdUser( Integer id){
       if(!userRepository.existsById(id)){
           throw new ResourceNotFoundException(USER_NOT_FOUND);
       }
       userRepository.deleteById(id);
    }
    // DELETE OWN ACCOUNT
    @Transactional
    public void ownAccountDelete( String email){
        User user = userRepository.findByUserEmail(email)
                .orElseThrow(()->new ResourceNotFoundException(USER_NOT_FOUND));
       userRepository.delete(user);
    }
    // ✅ ADD THIS METHOD (ENTITY)
    public User getId(Integer id){
      return  userRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND));
    }
}
