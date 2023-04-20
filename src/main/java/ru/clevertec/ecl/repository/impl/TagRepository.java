package ru.clevertec.ecl.repository.impl;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.config.transaction.Transaction;
import ru.clevertec.ecl.repository.AbstractRepository;
import ru.clevertec.ecl.entity.Tag;

@Transaction
@Repository
public class TagRepository extends AbstractRepository<Long, Tag> {
    private final SessionFactory sessionFactory;

    public TagRepository(SessionFactory sessionFactory) {
        super(sessionFactory, Tag.class);
        this.sessionFactory = sessionFactory;
    }

    public Tag findByName(String name) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT t FROM Tag t WHERE t.name=:name", Tag.class)
                .setParameter("name", name).getSingleResultOrNull();
    }

}