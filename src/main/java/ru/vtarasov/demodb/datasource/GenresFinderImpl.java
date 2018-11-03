package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class GenresFinderImpl implements GenresFinder {

    @Autowired
    private DataSourceFactory dsf;

    @Override
    public String[] findGenres() throws Exception {
        try (Connection conn = dsf.get().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select count(distinct genre) from Film")) {

            rs.next();

            String[] genres = new String[rs.getInt(1)];

            try(ResultSet rs2 = stmt.executeQuery("select distinct genre from Film")) {
                int idx = 0;
                while (rs2.next()) {
                    genres[idx++] = rs2.getString(1);
                }
            }

            return genres;
        }
    }
}
