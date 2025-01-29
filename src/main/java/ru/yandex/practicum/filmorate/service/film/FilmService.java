package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> getAllFilms();

    void addFilm(Film film);

    void updateFilm(Film film);

    Film getFilmById(Long filmId);

    void addLikeFilm(Long filmId, Long userId);

    void deleteLikeFilm(Long filmId, Long userId);

    List<Film> getPopularFilm(Integer countFilm);
}
