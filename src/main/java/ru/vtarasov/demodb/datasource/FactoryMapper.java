package ru.vtarasov.demodb.datasource;

import java.util.List;
import ru.vtarasov.demodb.model.Factory;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface FactoryMapper {

    void save(Factory f) throws Exception;
    void update(Factory f) throws Exception;
    void delete(Factory f) throws Exception;
    Factory load(int id) throws Exception;
    List<Factory> list() throws Exception;
}
