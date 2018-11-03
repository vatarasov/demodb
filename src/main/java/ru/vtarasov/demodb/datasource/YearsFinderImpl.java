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
public class YearsFinderImpl implements YearsFinder {

    @Autowired
    private DataSourceFactory dsf;

    @Override
    public int[] findYears() throws Exception {
        try (Connection conn = dsf.get().getConnection();
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
