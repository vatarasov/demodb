package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.Statement;
import ru.vtarasov.demodb.model.Country;


/**
 * @author vtarasov
 * @since 04.11.18
 */
public class FactoryRowGatewayImpl implements FactoryRowGateway {

    private DataSourceFactory dsf;

    private int id;
    private String name;
    private Country country;

    public FactoryRowGatewayImpl(DataSourceFactory dsf) {
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public void save() throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "insert into Factory (id, name, country) " +
                    "values (nextval('factory_id_seq'), '" + name + "', " +
                    (country != null ? String.valueOf(country.getId()) : "NULL") + ")");
        }
    }

    @Override
    public void update() throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "update Factory set name = '" + name + "', " +
                    "country = " + (country != null ? String.valueOf(country.getId()) : "NULL") + " " +
                    "where id = " + id);
        }
    }

    @Override
    public void delete() throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Factory where id = " + id);
        }
    }
}
