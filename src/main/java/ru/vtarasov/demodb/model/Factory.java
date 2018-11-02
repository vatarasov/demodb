package ru.vtarasov.demodb.model;

/**
 * @author vtarasov
 * @since 02.11.18
 */
public class Factory {
    private String name;
    private Country country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
