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
public class Country {
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

    public void save(DataSource ds) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("insert into Country (id, name) values (nextval('country_id_seq'), '" + name + "')");
        }
    }

    public void update(DataSource ds) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("update Country set name = '" + name + "' where id = " + id);
        }
    }

    public void delete(DataSource ds) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Country where id = " + id);
        }
    }

    public static Country load(DataSource ds, int id) throws SQLException {
        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name from Country where id = " + id)) {

            while (rs.next()) {
                Country country = new Country();
                country.setId(rs.getInt(1));
                country.setName(rs.getString(2));

                return country;
            }

            return null;
        }
    }

    public static List<Country> list(DataSource ds) throws SQLException {
        List<Country> countries = new ArrayList<>();

        try (Connection con = ds.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name from Country order by name")) {

            while (rs.next()) {
                Country country = new Country();
                country.setId(rs.getInt(1));
                country.setName(rs.getString(2));

                countries.add(country);
            }
        }

        return countries;
    }
}
