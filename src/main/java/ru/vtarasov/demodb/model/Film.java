package ru.vtarasov.demodb.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder.In;

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

    public Film(Map<String, Object> map) {
        id = (Integer) map.get("id");
        name = (String) map.get("name");
        year = (Integer) map.get("year");
        genre = (String) map.get("genre");
        description = (String) map.get("description");

        if (map.containsKey("factory")) {
            factory = new Factory((Map<String, Object>)map.get("factory"));
        }

        if (map.containsKey("producer")) {
            producer = new Man((Map<String, Object>)map.get("producer"));
        }

        stars = ((List<Map<String, Object>>)map.get("stars")).stream().map(Man::new).collect(Collectors.toList()).toArray(new Man[0]);
    }

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
