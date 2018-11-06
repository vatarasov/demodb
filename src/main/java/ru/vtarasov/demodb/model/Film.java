package ru.vtarasov.demodb.model;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.Type;

import static javax.persistence.FetchType.LAZY;

/**
 * @author vtarasov
 * @since 02.11.18
 */
@Entity
public class Film {
    @Id
    @SequenceGenerator(name="film_id_seq", sequenceName="film_id_seq")
    @GeneratedValue(generator="film_id_seq")
    private int id;


    private String name;
    private int year;
    private String genre;

    @ManyToOne
    @JoinColumn(name = "factory")
    private Factory factory;

    @ManyToMany
    @JoinTable(name = "filmstar", joinColumns = @JoinColumn(name = "film"), inverseJoinColumns = @JoinColumn(name = "star"))
    private Set<Man> stars;

    @ManyToOne
    @JoinColumn(name = "producer")
    private Man producer;

    private String description;

    @Lob
    @Basic(fetch = LAZY)
    @Type(type="org.hibernate.type.BinaryType")
    private byte[] image;

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

    public Set<Man> getStars() {
        return stars;
    }

    public void setStars(Set<Man> stars) {
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String starsToString() {
        return stars.stream().map(Man::getName).collect(Collectors.joining(", "));
    }
}
