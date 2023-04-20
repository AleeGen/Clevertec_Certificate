package ru.clevertec.ecl.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.clevertec.ecl.entity.BaseEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractRepository<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    private final SessionFactory sessionFactory;
    private final Class<E> clazz;

    @Override
    public List<E> findAll() {
        Session session = sessionFactory.getCurrentSession();
        var criteria = session.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return session.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<E> findById(K id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.get(clazz, id));
    }

    @Override
    public E save(E e) {
        sessionFactory.getCurrentSession().persist(e);
        return e;
    }

    @Override
    public E update(E e) {
        return sessionFactory.getCurrentSession().merge(e);
    }

    @Override
    public void delete(E e) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(e);
        session.flush();
    }

}