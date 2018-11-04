package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Country;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class CountryMapperImpl implements CountryMapper {

    @Autowired
    protected DataSourceFactory dsf;

    @Override
    public void save(Country c) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("insert into Country (id, name) values (nextval('country_id_seq'), '" + c.getName() + "')");
        }
    }

    @Override
    public void update(Country c) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("update Country set name = '" + c.getName() + "' where id = " + c.getId());
        }
    }

    @Override
    public void delete(Country c) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Country where id = " + c.getId());
        }
    }

    @Override
    public Country load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
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

    @Override
    public List<Country> list() throws Exception {
        List<Country> countries = new ArrayList<>();

        try (Connection con = dsf.get().getConnection();
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
