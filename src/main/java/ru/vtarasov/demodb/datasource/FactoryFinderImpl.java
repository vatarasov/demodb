package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Country;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class FactoryFinderImpl implements FactoryFinder {

    @Autowired
    private DataSourceFactory dsf;

    @Autowired
    private CountryFinder countryFinder;

    @Override
    public FactoryRowGateway load(int id) throws Exception {
        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Factory where id = " + id)) {

            while (rs.next()) {
                FactoryRowGateway gateway = new FactoryRowGatewayImpl(dsf);
                gateway.setId(rs.getInt(1));
                gateway.setName(rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    gateway.setCountry(new Country(countryFinder.load(country.intValue())));
                }

                return gateway;
            }

            return null;
        }
    }

    @Override
    public List<FactoryRowGateway> list() throws Exception {
        List<FactoryRowGateway> gateways = new ArrayList<>();

        try (Connection con = dsf.get().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select id, name, country from Factory order by name")) {

            while (rs.next()) {
                FactoryRowGateway gateway = new FactoryRowGatewayImpl(dsf);
                gateway.setId(rs.getInt(1));
                gateway.setName(rs.getString(2));

                Number country = (Number) rs.getObject(3);
                if (country != null) {
                    gateway.setCountry(new Country(countryFinder.load(country.intValue())));
                }

                gateways.add(gateway);
            }
        }

        return gateways;
    }
}
