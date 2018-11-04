package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class ManMapperImpl implements ManMapper {

    @Autowired
    private DataSourceFactory dsf;

    @Autowired
    private CountryMapper countryMapper;

    @Override
    public void save(Man man) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "insert into Man (id, name, country) " +
                    "values (nextval('man_id_seq'), '" + man.getName() + "', " +
                    (man.getCountry() != null ? String.valueOf(man.getCountry().getId()) : "NULL") + ")");
        }
    }

    @Override
    public void update(Man man) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "update Man set name = '" + man.getName() + "', " +
                    "country = " + (man.getCountry() != null ? String.valueOf(man.getCountry().getId()) : "NULL") + " " +
                    "where id = " + man.getId());
        }
    }

    @Override
    public void delete(Man man) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Man where id = " + man.getId());
        }
    }

    @Override
    public Man load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Man where id = " + id)) {

            while (rs.next()) {
                Man man = new Man();
                man.setId(rs.getInt(1));
                man.setName(rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    man.setCountry(countryMapper.load(country.intValue()));
                }

                return man;
            }

            return null;
        }
    }

    @Override
    public List<Man> list() throws Exception {
        List<Man> mans = new ArrayList<>();

        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Man order by name")) {

            while (rs.next()) {
                Man man = new Man();
                man.setId(rs.getInt(1));
                man.setName(rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    man.setCountry(countryMapper.load(country.intValue()));
                }

                mans.add(man);
            }
        }

        return mans;
    }
}
