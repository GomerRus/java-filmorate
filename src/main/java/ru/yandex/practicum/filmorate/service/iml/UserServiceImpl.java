package ru.yandex.practicum.filmorate.service.iml;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public void addUser(User user) {
        userStorage.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public void addFriendsUser(Long userId, Long friendId) {
        userStorage.addFriends(userId, friendId);
    }

    @Override
    public void deleteFriendsUser(Long userId, Long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    @Override
    public List<User> getAllFriendsUser(Long userId) {
        return List.copyOf(userStorage.getFriends(userId));
    }

    @Override
    public List<User> getCommonFriendsUser(Long userId, Long commonId) {
        return List.copyOf(userStorage.getCommonFriends(userId, commonId));
    }
}
