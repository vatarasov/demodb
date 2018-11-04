package ru.vtarasov.demodb.datasource;

import java.util.List;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface FactoryFinder {
    FactoryRowGateway load(int id) throws Exception;
    List<FactoryRowGateway> list() throws Exception;
}
