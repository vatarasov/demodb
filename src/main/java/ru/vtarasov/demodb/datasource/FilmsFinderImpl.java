package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Film;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class FilmsFinderImpl implements FilmsFinder {

    @Autowired
    private DataSourceFactory dsf;

    @Override
    public Film[] findFilms(String search, Set<Integer> years, Set<String> genres) throws SQLException {

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
                Film film = Film.load(dsf.get(), rs.getInt(1));

                films.add(film);
            }
        }

        return films.toArray(new Film[0]);
    }
}
