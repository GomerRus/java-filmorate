package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> usersStorage = new HashMap<>();
    private long id = 0L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(usersStorage.values());
    }

    @Override
    public void addUser(User user) {
        if (usersStorage.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id: " + user.getId() + " уже существует");
        }
        user.setId(++id);
        usersStorage.put(id, user);
    }

    @Override
    public void updateUser(User user) {
        if (usersStorage.containsKey(user.getId())) {
            usersStorage.put(user.getId(), user);
        } else {
            throw new NotFoundException("Запрашиваемый пользователь " + user.getId()
                    + " для обновления не найден. Повторите попытку");
        }
    }

    @Override
    public User getUserById(Long userId) {
        if (userId == null) {
            throw new ValidationException("ID пользователя не указан либо равен нулю. Укажите ID и повторите запрос.");
        }
        if (!usersStorage.containsKey(userId)) {
            throw new NotFoundException("Пользователя с ID: " + userId + " не существует.");
        }
        return usersStorage.get(userId);
    }
}
