package ru.yandex.practicum.filmorate.service.memory;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InMemoryUserService implements UserService {

    private final Map<Long, User> usersService = new HashMap<>();
    private long id = 0L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(usersService.values());
    }

    @Override
    public void addUser(User user) {
        if (usersService.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id: " + user.getId() + " уже существует");
        }
        user.setId(++id);
        usersService.put(id, user);
    }

    @Override
    public void updateUser(User user) {
        if (usersService.containsKey(user.getId())) {
            usersService.put(user.getId(), user);
        } else {
            throw new NotFoundException("Запрашиваемый пользователь " + user.getId() + " для обновления не найден. Повторите попытку");
        }
    }
}
