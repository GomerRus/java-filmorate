MERGE INTO mpa_rating (mpa_rating_id, mpa_name) values (1, 'G');
MERGE INTO mpa_rating (mpa_rating_id, mpa_name) values (2, 'PG');
MERGE INTO mpa_rating (mpa_rating_id, mpa_name) values (3, 'PG-13');
MERGE INTO mpa_rating (mpa_rating_id, mpa_name) values (4, 'R');
MERGE INTO mpa_rating (mpa_rating_id, mpa_name) values (5, 'NC-17');

MERGE INTO genre (genre_id, genre_name) values (1, 'Комедия');
MERGE INTO genre (genre_id, genre_name) values (2, 'Драма');
MERGE INTO genre (genre_id, genre_name) values (3, 'Мультфильм');
MERGE INTO genre (genre_id, genre_name) values (4, 'Триллер');
MERGE INTO genre (genre_id, genre_name) values (5, 'Документальный');
MERGE INTO genre (genre_id, genre_name) values (6, 'Боевик');
