package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.Statement;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public class CountryRowGatewayImpl implements CountryRowGateway {

    private DataSourceFactory dsf;

    private int id;
    private String name;

    public CountryRowGatewayImpl(DataSourceFactory dsf) {
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

    @Override
    public void save() throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("insert into Country (id, name) values (nextval('country_id_seq'), '" + name + "')");
        }
    }

    @Override
    public void update() throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("update Country set name = '" + name + "' where id = " + id);
        }
    }

    @Override
    public void delete() throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Country where id = " + id);
        }
    }
}
