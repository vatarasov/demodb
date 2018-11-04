package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import ru.vtarasov.demodb.model.Factory;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public class FilmRowGatewayImpl implements FilmRowGateway {

    private DataSourceFactory dsf;

    private int id;
    private String name;
    private int year;
    private String genre;

    private Factory factory;

    private Man[] stars;
    private Man producer;

    private String description;

    public FilmRowGatewayImpl(DataSourceFactory dsf) {
        this.dsf = dsf;
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

    @Override
    public void save() throws Exception {
        try (Connection con = dsf.get().getConnection();
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

    @Override
    public void update() throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            String genreVal = genre != null ? "'" + genre + "'" : "NULL";
            String factoryVal = factory != null ? String.valueOf(factory.getId()) : "NULL";
            String producerVal = producer != null ? String.valueOf(producer.getId()) : "NULL";
            String descriptionVal = description != null ? "'" + description + "'" : "NULL";

            stmt.executeUpdate(
                "update Film set name = '" + name + "', year = " + year + ", " +
                    "genre = " + genreVal + ", factory = " + factoryVal + ", " +
                    "producer = " + producerVal + ", " + "description = " + descriptionVal + " where id = " + id);

            stmt.executeUpdate("delete from Filmstar where film = " + id);

            for (Man star : stars) {
                stmt.executeUpdate("insert into Filmstar (star, film) values (" + star.getId() + ", " + id + ")");
            }
        }
    }

    @Override
    public void delete() throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Filmstar where film = " + id);
            stmt.executeUpdate("delete from Film where id = " + id);
        }
    }
}
