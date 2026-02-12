package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="role")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false,unique = true)
    String userRoleName;
    @Column(name = "create_at",updatable = false)
    LocalDateTime createTime;
    @Column(name = "update_at",updatable = true)
    LocalDateTime updateTime;
     @PrePersist
    public void prePersist(){
         this.createTime = LocalDateTime.now();
     }
     @PreUpdate
    public void preUpdate(){
         this.updateTime = LocalDateTime.now();
     }

    @ManyToMany(mappedBy = "userRoles",fetch = FetchType.EAGER)
    private List<User> users = new ArrayList<>();

}
