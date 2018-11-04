package ru.vtarasov.demodb.datasource;

import java.util.List;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface ManFinder {
    ManRowGateway load(int id) throws Exception;
    List<ManRowGateway> list() throws Exception;
}
