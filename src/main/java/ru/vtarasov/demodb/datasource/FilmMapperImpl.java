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
import ru.vtarasov.demodb.model.Film;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class FilmMapperImpl implements FilmMapper {

    @Autowired
    private DataSourceFactory dsf;

    @Autowired
    private FactoryMapper factoryMapper;

    @Autowired
    private ManMapper manMapper;

    @Override
    public void save(Film film) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            String genreVal = film.getGenre() != null ? "'" + film.getGenre() + "'" : "NULL";
            String factoryVal = film.getFactory() != null ? String.valueOf(film.getFactory().getId()) : "NULL";
            String producerVal = film.getProducer() != null ? String.valueOf(film.getProducer().getId()) : "NULL";
            String descriptionVal = film.getDescription() != null ? "'" + film.getDescription() + "'" : "NULL";

            stmt.executeUpdate(
                "insert into Film (id, name, year, genre, factory, producer, description) " +
                    "values (nextval('film_id_seq'), '" + film.getName() + "', " + film.getYear() + ", " +
                    genreVal + ", " + factoryVal + ", " + producerVal + ", " + descriptionVal + ")");

            try (ResultSet rs = stmt.executeQuery("select id from Film where name = '" + film.getName() + "' and year = " + film.getYear())) {
                rs.next();

                int id = rs.getInt(1);

                for (Man star : film.getStars()) {
                    stmt.executeUpdate("insert into Filmstar (star, film) values (" + star.getId() + ", " + id + ")");
                }
            }
        }
    }

    @Override
    public void update(Film film) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            String genreVal = film.getGenre() != null ? "'" + film.getGenre() + "'" : "NULL";
            String factoryVal = film.getFactory() != null ? String.valueOf(film.getFactory().getId()) : "NULL";
            String producerVal = film.getProducer() != null ? String.valueOf(film.getProducer().getId()) : "NULL";
            String descriptionVal = film.getDescription() != null ? "'" + film.getDescription() + "'" : "NULL";

            stmt.executeUpdate(
                "update Film set name = '" + film.getName() + "', year = " + film.getYear() + ", " +
                    "genre = " + genreVal + ", factory = " + factoryVal + ", " +
                    "producer = " + producerVal + ", " + "description = " + descriptionVal + " where id = " + film.getId());

            stmt.executeUpdate("delete from Filmstar where film = " + film.getId());

            for (Man star : film.getStars()) {
                stmt.executeUpdate("insert into Filmstar (star, film) values (" + star.getId() + ", " + film.getId() + ")");
            }
        }
    }

    @Override
    public void delete(Film film) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement()) {

            stmt.executeUpdate("delete from Filmstar where film = " + film.getId());
            stmt.executeUpdate("delete from Film where id = " + film.getId());
        }
    }

    @Override
    public Film load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, year, genre, factory, producer, description from Film where id = " + id)) {

            while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt(1));
                film.setName(rs.getString(2));
                film.setYear(rs.getInt(3));
                film.setGenre(rs.getString(4));
                film.setDescription(rs.getString(7));

                Number factory = (Number) rs.getObject(5);
                if (factory != null) {
                    film.setFactory(factoryMapper.load(factory.intValue()));
                }

                Number producer = (Number) rs.getObject(6);
                if (producer != null) {
                    film.setProducer(manMapper.load(producer.intValue()));
                }

                List<Man> stars = new ArrayList<>();
                try (Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery("select star from Filmstar where film = " + id)) {

                    while (rs2.next()) {
                        Man star = manMapper.load(rs2.getInt(1));
                        stars.add(star);
                    }
                }
                film.setStars(stars.toArray(new Man[0]));

                return film;
            }

            return null;
        }
    }

    @Override
    public List<Film> list() throws Exception {
        List<Film> films = new ArrayList<>();

        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, year, genre, factory, producer, description from Film order by name")) {

            while (rs.next()) {
                int id = rs.getInt(1);

                Film film = new Film();
                film.setId(id);
                film.setName(rs.getString(2));
                film.setYear(rs.getInt(3));
                film.setGenre(rs.getString(4));
                film.setDescription(rs.getString(7));

                Number factory = (Number) rs.getObject(5);
                if (factory != null) {
                    film.setFactory(factoryMapper.load(factory.intValue()));
                }

                Number producer = (Number) rs.getObject(6);
                if (producer != null) {
                    film.setProducer(manMapper.load(producer.intValue()));
                }

                List<Man> stars = new ArrayList<>();
                try (Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery("select star from Filmstar where film = " + id)) {

                    while (rs2.next()) {
                        Man star = manMapper.load(rs2.getInt(1));
                        stars.add(star);
                    }
                }
                film.setStars(stars.toArray(new Man[0]));

                films.add(film);
            }
        }

        return films;
    }

    @Override
    public List<Film> list(String search, Set<Integer> years, Set<String> genres) throws Exception {
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

        List<Film> films = new ArrayList<>();

        try(Connection conn = dsf.get().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            while (rs.next()) {
                Film film = load(rs.getInt(1));

                films.add(film);
            }
        }

        return films;
    }
}
