package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

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
        if (usersStorage.get(userId) == null) {
            throw new NotFoundException("Вы не указали ID пользователя. Повторите запрос.");
        }
        if (!usersStorage.containsKey(userId)) {
            throw new ValidationException("Пользователя с ID: " + userId + " не существует.");
        }
        return usersStorage.get(userId);
    }
}
