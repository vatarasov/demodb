package ru.vtarasov.demodb.model;

import ru.vtarasov.demodb.datasource.ManRowGateway;

/**
 * @author vtarasov
 * @since 02.11.18
 */
public class Man {

    private ManRowGateway gateway;

    public Man(ManRowGateway gateway) {
        this.gateway = gateway;
    }

    public ManRowGateway getGateway() {
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

    public Country getCountry() {
        return gateway.getCountry();
    }

    public void setCountry(Country country) {
        gateway.setCountry(country);
    }
}
