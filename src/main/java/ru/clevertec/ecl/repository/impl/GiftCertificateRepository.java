package ru.clevertec.ecl.repository.impl;

import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.config.transaction.Transaction;
import ru.clevertec.ecl.entity.Tag;
import ru.clevertec.ecl.repository.AbstractRepository;
import ru.clevertec.ecl.entity.GiftCertificate;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

@Transaction
@Repository
public class GiftCertificateRepository extends AbstractRepository<Long, GiftCertificate> {

    private final SessionFactory sessionFactory;

    public GiftCertificateRepository(SessionFactory sessionFactory) {
        super(sessionFactory, GiftCertificate.class);
        this.sessionFactory = sessionFactory;
    }

    public List<GiftCertificate> findByTagName(String name, boolean isDesc, String[] sortBy) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Tag> queryTag = builder.createQuery(Tag.class);
        Root<Tag> rootTag = queryTag.from(Tag.class);
        queryTag.where(builder.equal(rootTag.get("name"), name));
        Long idTag = session.createQuery(queryTag).getSingleResult().getId();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        Join<Object, Object> gct = root.join("tags");
        ParameterExpression<Long> id = builder.parameter(Long.class);
        Function<String, Order> f = isDesc ? s -> builder.desc(root.get(s)) : s -> builder.asc(root.get(s));
        List<Order> orders = Arrays.stream(sortBy).map(f).toList();
        query.where(builder.equal(gct.get("id"), id)).orderBy(orders);
        return session.createQuery(query).setParameter(id, idTag).getResultList();
    }

    public List<GiftCertificate> findByPart(String part, boolean isDesc, String[] sortBy) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = query.from(GiftCertificate.class);
        String pattern = "%" + part + "%";
        Predicate partName = builder.like(root.get("name"), pattern);
        Predicate partDescription = builder.like(root.get("description"), pattern);
        Function<String, Order> f = isDesc ? s -> builder.desc(root.get(s)) : s -> builder.asc(root.get(s));
        List<Order> orders = Arrays.stream(sortBy).map(f).toList();
        query.select(root).where(builder.or(partName, partDescription)).orderBy(orders);
        return session.createQuery(query).getResultList();
    }

}