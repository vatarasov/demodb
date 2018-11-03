package ru.vtarasov.demodb.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 * @author vtarasov
 * @since 02.11.18
 */
public class Factory {
    private int id;
    private String name;
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

    public void save(DataSource ds) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "insert into Factory (id, name, country) " +
                "values (nextval('factory_id_seq'), '" + name + "', " + (country != null ? String.valueOf(country.getId()) : "NULL") + ")");
        }
    }

    public void update(DataSource ds) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "update Factory set name = '" + name + "', country = " + (country != null ? String.valueOf(country.getId()) : "NULL") + " " +
                "where id = " + id);
        }
    }

    public void delete(DataSource ds) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Factory where id = " + id);
        }
    }

    public static Factory load(DataSource ds, int id) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Factory where id = " + id)) {

            while (rs.next()) {
                Factory factory = new Factory();
                factory.setId(rs.getInt(1));
                factory.setName(rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    factory.setCountry(Country.load(ds, country.intValue()));
                }

                return factory;
            }

            return null;
        }
    }

    public static List<Factory> list(DataSource ds) throws SQLException {
        List<Factory> factories = new ArrayList<>();

        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Factory order by name")) {

            while (rs.next()) {
                Factory factory = new Factory();
                factory.setId(rs.getInt(1));
                factory.setName(rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    factory.setCountry(Country.load(ds, country.intValue()));
                }

                factories.add(factory);
            }
        }

        return factories;
    }
}
