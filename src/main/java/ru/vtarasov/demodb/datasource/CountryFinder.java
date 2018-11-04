package ru.vtarasov.demodb.datasource;

import java.util.List;
import ru.vtarasov.demodb.model.Country;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface CountryFinder {
    Country load(int id);
    List<Country> list();
}
