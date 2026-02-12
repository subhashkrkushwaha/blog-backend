package com.example.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PostDto {
   private Integer id;
    private String title;
    private String content;
    private String image;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // Relations (flattened)
    private Integer userId;
    private Integer categoryId;
}
