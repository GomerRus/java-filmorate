package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class})
class FilmDbTest {

    @Autowired
    private FilmStorage filmStorage;

    Film setDefaultFilm() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1998, 8, 8));
        film.setDuration(160);
        Mpa mpa = new Mpa();
        mpa.setId(1);
        film.setMpa(mpa);
        film.setGenres(new ArrayList<>());
        return film;
    }

    @Test
    void addFilmTest() {
        Film addedFilm = filmStorage.addFilm(setDefaultFilm());
        assertThat(addedFilm).isNotNull();
        assertThat(addedFilm.getId()).isGreaterThan(0);
    }

    @Test
    void getFilmTest() {
        Film addedFilm = filmStorage.addFilm(setDefaultFilm());
        Film retrievedFilm = filmStorage.getFilmById(addedFilm.getId());
        assertThat(retrievedFilm).isNotNull();
        assertThat(retrievedFilm.getName()).isEqualTo(setDefaultFilm().getName());
    }

    @Test
    void getAllFilmsTest() {

        filmStorage.addFilm(setDefaultFilm());

        Film anotherFilm = new Film();
        anotherFilm.setName("Test Film");
        anotherFilm.setDescription("This is another test film.");
        anotherFilm.setReleaseDate(LocalDate.of(1990, 9, 9));
        anotherFilm.setDuration(90);
        Mpa mpa = new Mpa();
        mpa.setId(2);
        anotherFilm.setMpa(mpa);
        anotherFilm.setGenres(new ArrayList<>());
        filmStorage.addFilm(anotherFilm);

        List<Film> films = filmStorage.getAllFilms();
        assertThat(films.size()).isEqualTo(2);
    }
}