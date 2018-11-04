package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class CountryFinderImpl implements CountryFinder {

    @Autowired
    private DataSourceFactory dsf;

    @Override
    public CountryRowGateway load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name from Country where id = " + id)) {

            while (rs.next()) {
                CountryRowGateway gateway = new CountryRowGatewayImpl(dsf);
                gateway.setId(rs.getInt(1));
                gateway.setName(rs.getString(2));

                return gateway;
            }

            return null;
        }
    }

    @Override
    public List<CountryRowGateway> list() throws Exception {
        List<CountryRowGateway> gateways = new ArrayList<>();

        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name from Country order by name")) {

            while (rs.next()) {
                CountryRowGateway gateway = new CountryRowGatewayImpl(dsf);
                gateway.setId(rs.getInt(1));
                gateway.setName(rs.getString(2));

                gateways.add(gateway);
            }
        }

        return gateways;
    }
}
