package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    // GET /users — Получение списка пользователей
    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    // POST /users — Добавление нового пользователя
    @PostMapping
    public User create(@RequestBody User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (users.values().stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        long newId = getNextId();
        user.setId(newId);
        user.setRegistrationDate(Instant.now());
        users.put(newId, user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {

        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        User exisUser = users.get(user.getId());
        if (exisUser == null) {
            throw new ConditionsNotMetException("Пользователь с указанным Id не найден");
        }

        if (user.getEmail() != null && !user.getEmail().equals(exisUser.getEmail()) &&
                users.values().stream().anyMatch(existing -> existing.getEmail().equals(user.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        if (user.getUsername() != null) {
            exisUser.setUsername(user.getUsername());
        }
        if (user.getEmail() != null) {
            exisUser.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            exisUser.setPassword(user.getPassword());
        }

        return exisUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
