package ru.yandex.practicum.catsgram.model;

import lombok.Data;

@Data
public class Image {
    Long id;
    long  postId;
    String originalFileName;
    String filePath;
}
