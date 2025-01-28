package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void addUser(User user);

    void updateUser(User user);

    User getUserById(Long userId);

    void addFriendsUser(Long userId, Long friendId);

    void deleteFriendsUser(Long userId, Long friendId);

    List<User> getAllFriendsUser(Long userId);

    List<User> getCommonFriendsUser(Long userId, Long commonId);
}


