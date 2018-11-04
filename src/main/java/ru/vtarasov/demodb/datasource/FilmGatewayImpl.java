package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Factory;
import ru.vtarasov.demodb.model.Film;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class FilmGatewayImpl implements FilmGateway {

    @Autowired
    private DataSourceFactory dsf;

    @Autowired
    private FactoryGateway factoryGateway;

    @Autowired
    private ManGateway manGateway;

    @Override
    public void save(String name, int year, String genre, Integer factoryId, List<Integer> starIds, Integer producerId, String description)
        throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            String genreVal = genre != null ? "'" + genre + "'" : "NULL";
            String factoryVal = factoryId != null ? String.valueOf(factoryId) : "NULL";
            String producerVal = producerId != null ? String.valueOf(producerId) : "NULL";
            String descriptionVal = description != null ? "'" + description + "'" : "NULL";

            stmt.executeUpdate(
                "insert into Film (id, name, year, genre, factory, producer, description) " +
                    "values (nextval('film_id_seq'), '" + name + "', " + year + ", " +
                    genreVal + ", " + factoryVal + ", " + producerVal + ", " + descriptionVal + ")");

            try (ResultSet rs = stmt.executeQuery("select id from Film where name = '" + name + "' and year = " + year)) {
                rs.next();

                int id = rs.getInt(1);

                for (Integer starId : starIds) {
                    stmt.executeUpdate("insert into Filmstar (star, film) values (" + starId + ", " + id + ")");
                }
            }
        }
    }

    @Override
    public void update(int id, String name, int year, String genre, Integer factoryId, List<Integer> starIds, Integer producerId, String description)
        throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            String genreVal = genre != null ? "'" + genre + "'" : "NULL";
            String factoryVal = factoryId != null ? String.valueOf(factoryId) : "NULL";
            String producerVal = producerId != null ? String.valueOf(producerId) : "NULL";
            String descriptionVal = description != null ? "'" + description + "'" : "NULL";

            stmt.executeUpdate(
                "update Film set name = '" + name + "', year = " + year + ", " +
                    "genre = " + genreVal + ", factory = " + factoryVal + ", " +
                    "producer = " + producerVal + ", " + "description = " + descriptionVal + " where id = " + id);

            stmt.executeUpdate("delete from Filmstar where film = " + id);

            for (Integer starId : starIds) {
                stmt.executeUpdate("insert into Filmstar (star, film) values (" + starId + ", " + id + ")");
            }
        }
    }

    @Override
    public void delete(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Filmstar where film = " + id);
            stmt.executeUpdate("delete from Film where id = " + id);
        }
    }

    @Override
    public Map<String, Object> load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, year, genre, factory, producer, description from Film where id = " + id)) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", rs.getInt(1));
                map.put("name", rs.getString(2));
                map.put("year", rs.getInt(3));
                map.put("genre", rs.getString(4));
                map.put("description", rs.getString(7));

                Number factory = (Number) rs.getObject(5);
                if (factory != null) {
                    map.put("factory", factoryGateway.load(factory.intValue()));
                }

                Number producer = (Number) rs.getObject(6);
                if (producer != null) {
                    map.put("producer", manGateway.load(producer.intValue()));
                }

                List<Map<String, Object>> starsMap = new ArrayList<>();
                try (Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery("select star from Filmstar where film = " + id)) {

                    while (rs2.next()) {
                        Map<String, Object> starMap = manGateway.load(rs2.getInt(1));
                        starsMap.add(starMap);
                    }
                }

                map.put("stars", starsMap);

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
            ResultSet rs = stmt.executeQuery("select id, name, year, genre, factory, producer, description from Film order by name")) {

            while (rs.next()) {
                int id = rs.getInt(1);

                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("name", rs.getString(2));
                map.put("year", rs.getInt(3));
                map.put("genre", rs.getString(4));
                map.put("description", rs.getString(7));

                Number factory = (Number) rs.getObject(5);
                if (factory != null) {
                    map.put("factory", factoryGateway.load(factory.intValue()));
                }

                Number producer = (Number) rs.getObject(6);
                if (producer != null) {
                    map.put("producer", manGateway.load(producer.intValue()));
                }

                List<Map<String, Object>> starsMap = new ArrayList<>();
                try (Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery("select star from Filmstar where film = " + id)) {

                    while (rs2.next()) {
                        Map<String, Object> starMap = manGateway.load(rs2.getInt(1));
                        starsMap.add(starMap);
                    }
                }
                map.put("stars", starsMap);

                maps.add(map);
            }
        }

        return maps;
    }

    @Override
    public List<Map<String, Object>> list(String search, Set<Integer> years, Set<String> genres) throws Exception {
        String sql = "select id from Film where 1 = 1";

        if (search != null && !"".equals(search.trim())) {
            sql += " and name like '%" + search.trim() + "%'";
        }

        if (!years.isEmpty()) {
            sql += " and year in (" + years.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";
        }

        if (!genres.isEmpty()) {
            sql += " and genre in ('" + String.join("','", genres) + "')";
        }

        sql += " order by name";

        List<Map<String, Object>> maps = new ArrayList<>();

        try(Connection conn = dsf.get().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                Map<String, Object> map = load(rs.getInt(1));

                maps.add(map);
            }
        }

        return maps;
    }
}
