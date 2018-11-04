package ru.vtarasov.demodb.datasource;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import ru.vtarasov.demodb.model.Man;

/**
 * @author vtarasov
 * @since 04.11.18
 */
@Service
public class ManFinderImpl implements ManFinder {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Man load(int id) {
        return em.find(Man.class, id);
    }

    @Override
    public List<Man> list() {
        return em.createQuery("from " + Man.class.getName() + " order by name", Man.class).getResultList();
    }
}
