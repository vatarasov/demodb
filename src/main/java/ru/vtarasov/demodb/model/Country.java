package ru.vtarasov.demodb.model;

import ru.vtarasov.demodb.datasource.CountryRowGateway;

/**
 * @author vtarasov
 * @since 02.11.18
 */
public class Country {
    private CountryRowGateway gateway;

    public Country(CountryRowGateway gateway) {
        this.gateway = gateway;
    }

    public CountryRowGateway getGateway() {
        return gateway;
    }

    public int getId() {
        return gateway.getId();
    }

    public void setId(int id) {
        gateway.setId(id);
    }

    public String getName() {
        return gateway.getName();
    }

    public void setName(String name) {
        gateway.setName(name);
    }
}
