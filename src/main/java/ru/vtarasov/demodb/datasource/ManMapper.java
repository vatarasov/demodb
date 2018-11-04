package ru.vtarasov.demodb.datasource;

import java.util.List;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface ManMapper {
    void save(Man man) throws Exception;
    void update(Man man) throws Exception;
    void delete(Man man) throws Exception;
    Man load(int id) throws Exception;
    List<Man> list() throws Exception;
}
