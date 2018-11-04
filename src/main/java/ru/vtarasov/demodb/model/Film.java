package ru.vtarasov.demodb.model;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author vtarasov
 * @since 02.11.18
 */
public class Film {
    private int id;
    private String name;
    private int year;
    private String genre;

    private Factory factory;

    private Man[] stars;
    private Man producer;

    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    public Man[] getStars() {
        return stars;
    }

    public void setStars(Man[] stars) {
        this.stars = stars;
    }

    public String starsToString() {
        return Arrays.stream(stars).map(Man::getName).collect(Collectors.joining(", "));
    }

    public Man getProducer() {
        return producer;
    }

    public void setProducer(Man producer) {
        this.producer = producer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
