package ru.vtarasov.demodb.datasource;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Factory;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class FactoryFinderImpl implements FactoryFinder {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Factory load(int id) throws Exception {
        return em.find(Factory.class, id);
    }

    @Override
    public List<Factory> list() throws Exception {
        return em.createQuery("from " + Factory.class.getName() + " order by name", Factory.class).getResultList();
    }
}
