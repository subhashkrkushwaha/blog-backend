package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name="posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false)
    String title;
    @Column(nullable = false,columnDefinition = "TEXT")
    String content;
    @Column
    String image;
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
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", nullable = false)
    Category category;

}
