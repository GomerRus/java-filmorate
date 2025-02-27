package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class UserDbTest {
    @Autowired
    private UserStorage userStorage;

    private User setDefaultUser() {
        User user = new User();
        user.setName("Name");
        user.setEmail("User@bk.ru");
        user.setBirthday(LocalDate.of(1999, 9, 9));
        user.setLogin("login");
        return user;
    }

    @Test
    void getAllUsers() {
        User addedUser = userStorage.addUser(setDefaultUser());
        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isGreaterThan(0);
    }

    @Test
    void addUserTest() {
        User addedUser = userStorage.addUser(setDefaultUser());
        assertThat(addedUser).isNotNull();
        assertThat(addedUser.getId()).isGreaterThan(0);
    }


    @Test
    void getUserTest() {
        User addedUser = userStorage.addUser(setDefaultUser());
        User retrievedUser = userStorage.getUserById(addedUser.getId());
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getName()).isEqualTo(setDefaultUser().getName());
    }

    @Test
    void getAllUsersTest() {
        userStorage.addUser(setDefaultUser());
        User anotherUser = new User();
        anotherUser.setName("Test User");
        anotherUser.setEmail("User2@bk.ru");
        anotherUser.setBirthday(LocalDate.of(2000, 10, 10));
        anotherUser.setLogin("login2");
        userStorage.addUser(anotherUser);
        List<User> users = userStorage.getAllUsers();
        assertThat(users.size()).isEqualTo(2);
    }
}