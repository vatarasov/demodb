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
public class CountryGatewayImpl implements CountryGateway {

    @Autowired
    protected DataSourceFactory dsf;

    @Override
    public void save(String name) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("insert into Country (id, name) values (nextval('country_id_seq'), '" + name + "')");
        }
    }

    @Override
    public void update(int id, String name) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("update Country set name = '" + name + "' where id = " + id);
        }
    }

    @Override
    public void delete(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Country where id = " + id);
        }
    }

    @Override
    public Map<String, Object> load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name from Country where id = " + id)) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt(1));
                map.put("name", rs.getString(2));

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
            ResultSet rs = stmt.executeQuery("select id, name from Country order by name")) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt(1));
                map.put("name", rs.getString(2));

                maps.add(map);
            }
        }

        return maps;
    }
}
