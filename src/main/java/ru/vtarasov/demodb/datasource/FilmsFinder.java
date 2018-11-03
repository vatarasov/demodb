package ru.vtarasov.demodb.datasource;

import java.util.Set;
import ru.vtarasov.demodb.model.Film;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface FilmsFinder {
    Film[] findFilms(String search, Set<Integer> years, Set<String> genres) throws Exception;
}
