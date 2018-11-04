package ru.vtarasov.demodb.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ru.vtarasov.demodb.datasource.FilmRowGateway;

/**
 * @author vtarasov
 * @since 02.11.18
 */
public class Film {

    private FilmRowGateway gateway;

    public Film(FilmRowGateway gateway) {
        this.gateway = gateway;
    }

    public FilmRowGateway getGateway() {
        return gateway;
    }

    public int getId() {
        return gateway.getId();
    }

    public void setId(int id) {
        gateway.setId(id);
    }

    public String getName() {
        return gateway.getName();
    }

    public void setName(String name) {
        gateway.setName(name);
    }

    public int getYear() {
        return gateway.getYear();
    }

    public void setYear(int year) {
        gateway.setYear(year);
    }

    public String getGenre() {
        return gateway.getGenre();
    }

    public void setGenre(String genre) {
        gateway.setGenre(genre);
    }

    public Factory getFactory() {
        return gateway.getFactory();
    }

    public void setFactory(Factory factory) {
        gateway.setFactory(factory);
    }

    public Man[] getStars() {
        return gateway.getStars();
    }

    public void setStars(Man[] stars) {
        gateway.setStars(stars);
    }

    public String starsToString() {
        return Arrays.stream(gateway.getStars()).map(Man::getName).collect(Collectors.joining(", "));
    }

    public Man getProducer() {
        return gateway.getProducer();
    }

    public void setProducer(Man producer) {
        gateway.setProducer(producer);
    }

    public String getDescription() {
        return gateway.getDescription();
    }

    public void setDescription(String description) {
        gateway.setDescription(description);
    }
}
