package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Добавляем пользователя: {}", user.getLogin());
        userService.addUser(user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Обновляем пользователя с логином: {}", user.getLogin());
        userService.updateUser(user);
        return user;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        log.info("Получаем пользователя с ID: {}", id);
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriendsUser(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Добавляем в друзья пользователя c ID: {}", id);
        userService.addFriendsUser(id, friendId);
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriendsUser(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Удаляем из друзей пользователя с ID: {}", id);
        userService.deleteFriendsUser(id, friendId);
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriendsUser(@PathVariable Long id) {
        log.info("Получаем список всех друзей");
        return userService.getAllFriendsUser(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsUser(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получаем список общих друзей с другим пользователем");
        return userService.getCommonFriendsUser(id, otherId);
    }
}
