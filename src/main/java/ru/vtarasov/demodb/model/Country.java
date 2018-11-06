package ru.vtarasov.demodb.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * @author vtarasov
 * @since 02.11.18
 */
@Entity
public class Country {
    @Id
    @SequenceGenerator(name = "country_id_seq", sequenceName = "country_id_seq")
    @GeneratedValue(generator="country_id_seq")
    private int id;

    private String name;

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
