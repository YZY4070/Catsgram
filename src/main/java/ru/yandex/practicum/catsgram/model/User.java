package ru.yandex.practicum.catsgram.model;

import lombok.Data;

import java.time.Instant;

@Data
public class User {
    Long id;
    String username;
    String email;
    String password;
    Instant registrationDate;
}
