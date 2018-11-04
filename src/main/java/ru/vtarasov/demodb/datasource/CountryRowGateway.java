package ru.vtarasov.demodb.datasource;

import java.util.List;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface CountryRowGateway {
    int getId();
    void setId(int id);
    String getName();
    void setName(String name);

    void save() throws Exception;
    void update() throws Exception;
    void delete() throws Exception;
}
