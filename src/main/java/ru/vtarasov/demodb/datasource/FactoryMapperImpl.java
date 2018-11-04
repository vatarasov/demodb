package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Factory;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class FactoryMapperImpl implements FactoryMapper {

    @Autowired
    private DataSourceFactory dsf;

    @Autowired
    private CountryMapper countryMapper;

    @Override
    public void save(Factory f) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "insert into Factory (id, name, country) " +
                    "values (nextval('factory_id_seq'), '" + f.getName() + "', " +
                    (f.getCountry() != null ? String.valueOf(f.getCountry().getId()) : "NULL") + ")");
        }
    }

    @Override
    public void update(Factory f) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "update Factory set name = '" + f.getName() + "', " +
                    "country = " + (f.getCountry() != null ? String.valueOf(f.getCountry().getId()) : "NULL") + " " +
                    "where id = " + f.getId());
        }
    }

    @Override
    public void delete(Factory f) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Factory where id = " + f.getId());
        }
    }

    @Override
    public Factory load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Factory where id = " + id)) {

            while (rs.next()) {
                Factory factory = new Factory();
                factory.setId(rs.getInt(1));
                factory.setName(rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    factory.setCountry(countryMapper.load(country.intValue()));
                }

                return factory;
            }

            return null;
        }
    }

    @Override
    public List<Factory> list() throws Exception {
        List<Factory> factories = new ArrayList<>();

        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Factory order by name")) {

            while (rs.next()) {
                Factory factory = new Factory();
                factory.setId(rs.getInt(1));
                factory.setName(rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    factory.setCountry(countryMapper.load(country.intValue()));
                }

                factories.add(factory);
            }
        }

        return factories;
    }
}
