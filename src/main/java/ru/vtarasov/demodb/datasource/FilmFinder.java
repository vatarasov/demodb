package ru.vtarasov.demodb.datasource;

import java.util.List;
import java.util.Set;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface FilmFinder {
    FilmRowGateway load(int id) throws Exception;
    List<FilmRowGateway> list() throws Exception;
    List<FilmRowGateway> list(String search, Set<Integer> years, Set<String> genres) throws Exception;
}
