package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "category")
public class Category {

    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Id
    Integer id;
    @Column(nullable = false)
    String category;
}
