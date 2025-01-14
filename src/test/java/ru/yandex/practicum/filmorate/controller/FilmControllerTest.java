package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private Validator validator;
    Film film;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        film = Film.builder()
                .name("Пираты Карибского моря")
                .description("Проклятие Чёрной жемчужины» — приключенческий фильм о пиратах," +
                        " действие которого разворачивается на Карибах в первой половине XVIII века. 2")
                .releaseDate(LocalDate.of(2003, 7, 9))
                .duration(143)
                .build();
    }

    @Test
    void noAddFilmWithEmptyNameTest() {
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    void noAddFilmWithDescriptionMore200SymbolsTest() {
        film.setDescription(film.getDescription() + film.getDescription());
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void noAddFilmWithInvalidReleaseDateTest() {
        film.setReleaseDate(LocalDate.of(1894, 12, 28));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void noAddFilmWhenFilmDurationIsNullTest() {
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void noAddFilmWithNegativeDurationTest() {
        film.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}
