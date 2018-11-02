package ru.vtarasov.demodb.model;

/**
 * @author vtarasov
 * @since 02.11.18
 */
public class Film {
    private String name;
    private int year;
    private String genre;

    private Man[] stars;
    private Man producer;

    private String description;

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

    public Man[] getStars() {
        return stars;
    }

    public void setStars(Man[] stars) {
        this.stars = stars;
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
