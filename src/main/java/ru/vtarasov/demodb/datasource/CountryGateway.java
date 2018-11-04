package ru.vtarasov.demodb.datasource;

import java.util.List;
import java.util.Map;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface CountryGateway {
    void save(String name) throws Exception;
    void update(int id, String name) throws Exception;
    void delete(int id) throws Exception;
    Map<String, Object> load(int id) throws Exception;

    List<Map<String, Object>> list() throws Exception;
}
