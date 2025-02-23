package ru.yandex.practicum.filmorate.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(friendId);
        user.getFriendsId().add(friendId);
        userFriend.getFriendsId().add(userId);
    }

    @Override
    public void deleteFriendsUser(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        User userFriend = userStorage.getUserById(friendId);
        user.getFriendsId().remove(friendId);
        userFriend.getFriendsId().remove(userId);
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
        Set<Long> friendsCommon = userStorage.getUserById(commonId).getFriendsId();
        for (Long friendId : userStorage.getUserById(userId).getFriendsId()) {
            if (friendsCommon.contains(friendId)) {
                friendsUser.add(userStorage.getUserById(friendId));
            }
        }
        return friendsUser;
    }
}
