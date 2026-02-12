package com.example.blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {


    private Integer id;
    private String userName;
    @NotEmpty
    private String userEmail;
    @JsonIgnore
    @NotEmpty
    private String password;
    private LocalDateTime currentTime;
    private List<UserRoleDto> userRoles;

    public UserDto(Integer id, String userEmail, String userName) {
        this.id = id;
        this.userEmail = userEmail;
        this.userName = userName;
    }


}
