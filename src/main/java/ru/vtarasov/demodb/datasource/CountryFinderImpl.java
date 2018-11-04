package ru.vtarasov.demodb.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Country;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class CountryFinderImpl implements CountryFinder {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Country load(int id){
        return em.find(Country.class, id);
    }

    @Override
    public List<Country> list() {
        return em.createQuery("from " + Country.class.getName() + " order by name", Country.class).getResultList();
    }
}
