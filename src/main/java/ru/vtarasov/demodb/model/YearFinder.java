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
public class YearFinder {

    public static int[] findYears(DataSource ds) throws SQLException {
        try (Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select count(distinct year) from Film")) {

            rs.next();

            int[] years = new int[rs.getInt(1)];

            try(ResultSet rs2 = stmt.executeQuery("select distinct year from Film")) {
                int idx = 0;
                while (rs2.next()) {
                    years[idx++] = rs2.getInt(1);
                }
            }

            return years;
        }
    }
}
