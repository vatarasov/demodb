package ru.vtarasov.demodb.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

/**
 * @author vtarasov
 * @since 04.11.18
 */
public class GenresFinder {

    public static String[] findGenres(DataSource ds) throws SQLException {
        try (Connection conn = ds.getConnection();
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
