package ru.vtarasov.demodb.model;

import java.util.Map;

/**
 * @author vtarasov
 * @since 02.11.18
 */
public class Factory {
    private int id;
    private String name;
    private Country country;

    public Factory(Map<String, Object> map) {
        this.id = (Integer) map.get("id");
        this.name = (String) map.get("name");

        if (map.containsKey("country")) {
            this.country = new Country((Map<String, Object>)map.get("country"));
        }
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
