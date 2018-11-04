package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Factory;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class FilmFinderImpl implements FilmFinder {

    @Autowired
    private DataSourceFactory dsf;

    @Autowired
    private FactoryFinder factoryFinder;

    @Autowired
    private ManFinder manFinder;

    @Override
    public FilmRowGateway load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, year, genre, factory, producer, description from Film where id = " + id)) {

            while (rs.next()) {
                FilmRowGateway gateway = new FilmRowGatewayImpl(dsf);
                gateway.setId(rs.getInt(1));
                gateway.setName(rs.getString(2));
                gateway.setYear(rs.getInt(3));
                gateway.setGenre(rs.getString(4));
                gateway.setDescription(rs.getString(7));

                Number factory = (Number) rs.getObject(5);
                if (factory != null) {
                    gateway.setFactory(new Factory(factoryFinder.load(factory.intValue())));
                }

                Number producer = (Number) rs.getObject(6);
                if (producer != null) {
                    gateway.setProducer(new Man(manFinder.load(producer.intValue())));
                }

                List<Man> stars = new ArrayList<>();
                try (Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery("select star from Filmstar where film = " + id)) {

                    while (rs2.next()) {
                        Man star = new Man(manFinder.load(rs2.getInt(1)));
                        stars.add(star);
                    }
                }
                gateway.setStars(stars.toArray(new Man[0]));

                return gateway;
            }

            return null;
        }
    }

    @Override
    public List<FilmRowGateway> list() throws Exception {
        List<FilmRowGateway> gateways = new ArrayList<>();

        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, year, genre, factory, producer, description from Film order by name")) {

            while (rs.next()) {
                int id = rs.getInt(1);

                FilmRowGateway gateway = new FilmRowGatewayImpl(dsf);
                gateway.setId(id);
                gateway.setName(rs.getString(2));
                gateway.setYear(rs.getInt(3));
                gateway.setGenre(rs.getString(4));
                gateway.setDescription(rs.getString(7));

                Number factory = (Number) rs.getObject(5);
                if (factory != null) {
                    gateway.setFactory(new Factory(factoryFinder.load(factory.intValue())));
                }

                Number producer = (Number) rs.getObject(6);
                if (producer != null) {
                    gateway.setProducer(new Man(manFinder.load(producer.intValue())));
                }

                List<Man> stars = new ArrayList<>();
                try (Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery("select star from Filmstar where film = " + id)) {

                    while (rs2.next()) {
                        Man star = new Man(manFinder.load(rs2.getInt(1)));
                        stars.add(star);
                    }
                }
                gateway.setStars(stars.toArray(new Man[0]));

                gateways.add(gateway);
            }
        }

        return gateways;
    }

    @Override
    public List<FilmRowGateway> list(String search, Set<Integer> years, Set<String> genres) throws Exception {
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

        List<FilmRowGateway> gateways = new ArrayList<>();

        try(Connection conn = dsf.get().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                FilmRowGateway gateway = load(rs.getInt(1));

                gateways.add(gateway);
            }
        }

        return gateways;
    }
}
