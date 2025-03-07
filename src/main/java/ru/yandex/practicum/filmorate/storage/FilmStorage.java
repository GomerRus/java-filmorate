package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();

    Film addFilm(Film film);

    void updateFilm(Film film);

    Film getFilmById(Long filmId);

    List<Film> getPopularFilmOnLike(int count);
}
