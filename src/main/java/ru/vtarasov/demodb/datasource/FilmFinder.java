package ru.vtarasov.demodb.datasource;

import java.util.List;
import java.util.Set;
import ru.vtarasov.demodb.model.Film;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface FilmFinder {
    Film load(int id);
    List<Film> list();
    List<Film> list(String search, Set<Integer> years, Set<String> genres);
}
