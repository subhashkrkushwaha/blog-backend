package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false)
    String userName;
    @Column(nullable = false,unique = true)
    String userEmail;
    @Column(nullable = false)
    String password;
    @Column(name="created_at",updatable = false)
    LocalDateTime currentTime;
    @PrePersist
    public void prePersist(){
        currentTime =  LocalDateTime.now();
    }
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns =  @JoinColumn(name ="user_id"),
           inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private List<UserRole> userRoles = new ArrayList<>();
    // For Bi-directional Mapping
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
}
