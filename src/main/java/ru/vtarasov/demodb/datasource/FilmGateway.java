package ru.vtarasov.demodb.datasource;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface FilmGateway {
    void save(String name, int year, String genre, Integer factoryId, List<Integer> starIds, Integer producerId, String description)
        throws Exception;
    void update(int id, String name, int year, String genre, Integer factoryId, List<Integer> starIds, Integer producerId, String description)
        throws Exception;
    void delete(int id) throws Exception;
    Map<String, Object> load(int id) throws Exception;
    List<Map<String, Object>> list() throws Exception;
    List<Map<String, Object>> list(String search, Set<Integer> years, Set<String> genres) throws Exception;
}
