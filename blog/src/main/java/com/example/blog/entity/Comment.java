package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;
    @Column(nullable = false)
    String comment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "post_id", nullable = false)
    Post post;
}
