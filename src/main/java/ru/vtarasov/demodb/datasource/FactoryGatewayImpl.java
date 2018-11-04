package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class FactoryGatewayImpl implements FactoryGateway {

    @Autowired
    private DataSourceFactory dsf;

    @Autowired
    private CountryGateway countryGateway;

    @Override
    public void save(String name, Integer countryId) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "insert into Factory (id, name, country) " +
                    "values (nextval('factory_id_seq'), '" + name + "', " +
                    (countryId != null ? String.valueOf(countryId) : "NULL") + ")");
        }
    }

    @Override
    public void update(int id, String name, Integer countryId) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate(
                "update Factory set name = '" + name + "', " +
                    "country = " + (countryId != null ? String.valueOf(countryId) : "NULL") + " " +
                    "where id = " + id);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Factory where id = " + id);
        }
    }

    @Override
    public Map<String, Object> load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Factory where id = " + id)) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt(1));
                map.put("name", rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    map.put("country", countryGateway.load(country.intValue()));
                }

                return map;
            }

            return null;
        }
    }

    @Override
    public List<Map<String, Object>> list() throws Exception {
        List<Map<String, Object>> maps = new ArrayList<>();

        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Factory order by name")) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt(1));
                map.put("name", rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    map.put("country", countryGateway.load(country.intValue()));
                }

                maps.add(map);
            }
        }

        return maps;
    }
}
