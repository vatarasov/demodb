package ru.vtarasov.demodb.datasource;

import java.util.List;
import java.util.Map;
import ru.vtarasov.demodb.model.Country;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface ManRowGateway {
    int getId();
    void setId(int id);
    String getName();
    void setName(String name);
    Country getCountry();
    void setCountry(Country country);

    void save() throws Exception;
    void update() throws Exception;
    void delete() throws Exception;
}
