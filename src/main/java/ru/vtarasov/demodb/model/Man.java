package ru.vtarasov.demodb.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 * @author vtarasov
 * @since 02.11.18
 */
@Entity
public class Man {

    @Id
    @SequenceGenerator(name="man_id_seq", sequenceName="man_id_seq")
    private int id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "country")
    private Country country;

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
