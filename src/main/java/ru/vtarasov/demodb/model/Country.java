package ru.vtarasov.demodb.model;

import java.util.Map;

/**
 * @author vtarasov
 * @since 02.11.18
 */
public class Country {
    private int id;
    private String name;

    public Country(Map<String, Object> map) {
        id = (Integer) map.get("id");
        name = (String) map.get("name");
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
}
