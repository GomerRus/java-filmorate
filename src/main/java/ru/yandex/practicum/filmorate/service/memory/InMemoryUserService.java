package ru.yandex.practicum.filmorate.service.memory;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class InMemoryUserService implements UserService {
    private UserStorage userStorage;

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
        userStorage.getUserById(userId).getFriendsId().add(friendId);
        userStorage.getUserById(friendId).getFriendsId().add(userId);
    }

    @Override
    public void deleteFriendsUser(Long userId, Long friendId) {
        userStorage.getUserById(userId).getFriendsId().remove(friendId);
        userStorage.getUserById(friendId).getFriendsId().remove(userId);
    }

    @Override
    public List<User> getAllFriendsUser(Long userId) {
        List<User> friendsUser = new ArrayList<>();
        for (Long friendId : userStorage.getUserById(userId).getFriendsId()) {
            friendsUser.add(userStorage.getUserById(friendId));
        }
        return friendsUser;
    }

    @Override
    public List<User> getCommonFriendsUser(Long userId, Long commonId) {
        List<User> friendsUser = new ArrayList<>();
        for (Long friendId : userStorage.getUserById(userId).getFriendsId()) {
            if (userStorage.getUserById(commonId).getFriendsId().contains(friendId)) {
                friendsUser.add(userStorage.getUserById(friendId));
            }
        }
        return friendsUser;
    }
}
