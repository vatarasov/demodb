package ru.vtarasov.demodb.datasource;

import java.util.List;
import java.util.Set;
import ru.vtarasov.demodb.model.Film;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface FilmMapper {
    void save(Film film) throws Exception;
    void update(Film film) throws Exception;
    void delete(Film film) throws Exception;
    Film load(int id) throws Exception;
    List<Film> list() throws Exception;
    List<Film> list(String search, Set<Integer> years, Set<String> genres) throws Exception;
}
