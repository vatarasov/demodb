package ru.vtarasov.demodb.datasource;

import java.util.List;
import java.util.Set;
import ru.vtarasov.demodb.model.Factory;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public interface FilmRowGateway {
    int getId();
    void setId(int id);
    String getName();
    void setName(String name);
    int getYear();
    void setYear(int year);
    String getGenre();
    void setGenre(String genre);
    Factory getFactory();
    void setFactory(Factory factory);
    Man[] getStars();
    void setStars(Man[] stars);
    Man getProducer();
    void setProducer(Man producer);
    String getDescription();
    void setDescription(String description);

    void save() throws Exception;
    void update() throws Exception;
    void delete() throws Exception;
}
