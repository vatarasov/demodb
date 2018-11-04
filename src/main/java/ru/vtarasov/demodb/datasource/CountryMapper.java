package ru.vtarasov.demodb.datasource;

import java.util.List;
import ru.vtarasov.demodb.model.Country;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface CountryMapper {
    void save(Country c) throws Exception;
    void update(Country c) throws Exception;
    void delete(Country c) throws Exception;
    Country load(int id) throws Exception;

    List<Country> list() throws Exception;
}
