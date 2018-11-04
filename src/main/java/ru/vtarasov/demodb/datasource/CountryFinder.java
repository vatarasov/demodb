package ru.vtarasov.demodb.datasource;

import java.util.List;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface CountryFinder {
    CountryRowGateway load(int id) throws Exception;
    List<CountryRowGateway> list() throws Exception;
}
