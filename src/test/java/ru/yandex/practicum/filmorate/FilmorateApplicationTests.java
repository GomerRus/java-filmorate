package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.GenreController;
import ru.yandex.practicum.filmorate.controller.MpaController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@AutoConfigureTestDatabase
public class FilmorateApplicationTests {

    @Autowired
    private FilmController filmController;

    @Autowired
    private UserController userController;
    @Autowired
    private FilmService filmService;

    @Autowired
    private UserService userService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private MpaService mpaService;

    @Autowired
    private GenreController genreController;

    @Autowired
    private MpaController mpaController;

    @Test
    void contextLoads() {
        assertThat(filmController).isNotNull();
        assertThat(userController).isNotNull();
        assertThat(filmService).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(genreService).isNotNull();
        assertThat(mpaService).isNotNull();
        assertThat(genreController).isNotNull();
        assertThat(mpaController).isNotNull();
    }
}