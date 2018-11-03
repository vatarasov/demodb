package ru.vtarasov.demodb.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;

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

    public void save(DataSource ds) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement()) {

            String genreVal = genre != null ? "'" + genre + "'" : "NULL";
            String factoryVal = factory != null ? String.valueOf(factory.getId()) : "NULL";
            String producerVal = producer != null ? String.valueOf(producer.getId()) : "NULL";
            String descriptionVal = description != null ? "'" + description + "'" : "NULL";

            stmt.executeUpdate(
                "insert into Film (id, name, year, genre, factory, producer, description) " +
                    "values (nextval('film_id_seq'), '" + name + "', " + year + ", " +
                    genreVal + ", " + factoryVal + ", " + producerVal + ", " + descriptionVal + ")");

            try (ResultSet rs = stmt.executeQuery("select id from Film where name = '" + name + "' and year = " + year)) {
                rs.next();

                int id = rs.getInt(1);

                for (Man star : stars) {
                    stmt.executeUpdate("insert into Filmstar (star, film) values (" + star.getId() + ", " + id + ")");
                }
            }
        }
    }

    public void update(DataSource ds) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement()) {

            String genreVal = genre != null ? "'" + genre + "'" : "NULL";
            String factoryVal = factory != null ? String.valueOf(factory.getId()) : "NULL";
            String producerVal = producer != null ? String.valueOf(producer.getId()) : "NULL";
            String descriptionVal = description != null ? "'" + description + "'" : "NULL";

            stmt.executeUpdate(
                "update Film set name = '" + name + "', year = " + year + ", genre = " + genreVal + ", factory = " + factoryVal + ", " +
                    "producer = " + producerVal + ", " + "description = " + descriptionVal + " where id = " + id);

            stmt.executeUpdate("delete from Filmstar where film = " + id);

            for (Man star : stars) {
                stmt.executeUpdate("insert into Filmstar (star, film) values (" + star.getId() + ", " + id + ")");
            }
        }
    }

    public void delete(DataSource ds) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Filmstar where film = " + id);
            stmt.executeUpdate("delete from Film where id = " + id);
        }
    }

    public static Film load(DataSource ds, int id) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, year, genre, factory, producer, description from Film where id = " + id)) {

            while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt(1));
                film.setName(rs.getString(2));
                film.setYear(rs.getInt(3));
                film.setGenre(rs.getString(4));
                film.setDescription(rs.getString(7));

                Number factory = (Number) rs.getObject(5);
                if (factory != null) {
                    film.setFactory(Factory.load(ds, factory.intValue()));
                }

                Number producer = (Number) rs.getObject(6);
                if (producer != null) {
                    film.setProducer(Man.load(ds, producer.intValue()));
                }

                List<Man> stars = new ArrayList<>();
                try (Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery("select star from Filmstar where film = " + id)) {

                    while (rs2.next()) {
                        Man star = Man.load(ds, rs2.getInt(1));
                        stars.add(star);
                    }
                }
                film.setStars(stars.toArray(new Man[0]));

                return film;
            }

            return null;
        }
    }

    public static List<Film> list(DataSource ds) throws SQLException {
        List<Film> films = new ArrayList<>();

        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, year, genre, factory, producer, description from Film order by name")) {

            while (rs.next()) {
                int id = rs.getInt(1);

                Film film = new Film();
                film.setId(id);
                film.setName(rs.getString(2));
                film.setYear(rs.getInt(3));
                film.setGenre(rs.getString(4));
                film.setDescription(rs.getString(7));

                Number factory = (Number) rs.getObject(5);
                if (factory != null) {
                    film.setFactory(Factory.load(ds, factory.intValue()));
                }

                Number producer = (Number) rs.getObject(6);
                if (producer != null) {
                    film.setProducer(Man.load(ds, producer.intValue()));
                }

                List<Man> stars = new ArrayList<>();
                try (Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery("select star from Filmstar where film = " + id)) {

                    while (rs2.next()) {
                        Man star = Man.load(ds, rs2.getInt(1));
                        stars.add(star);
                    }
                }
                film.setStars(stars.toArray(new Man[0]));

                films.add(film);
            }
        }

        return films;
    }
}
