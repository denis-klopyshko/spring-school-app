package com.example.dao.impl;

import com.example.dao.GroupDao;
import com.example.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

@Repository
public class GroupDaoImpl implements GroupDao {
    public static final String FIND_BY_NAME = "select g from Group g where g.name = :name";
    private static final String FIND_ALL_SQL = "select g from Group g";
    private static final String COUNT_QUERY = "select count(g) from Group g";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Group> findAll() {
        return em.createQuery(FIND_ALL_SQL, Group.class)
                .getResultList();
    }

    @Override
    public List<Group> findAllWithLessOrEqualStudents(Long studentsQuantity) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Group> criteriaQuery = criteriaBuilder.createQuery(Group.class);
        Root<Group> root = criteriaQuery.from(Group.class);
        Join<Group, Student> studentsJoin = root.join(Group_.students, JoinType.LEFT);
        criteriaQuery.groupBy(root);
        Expression<Long> count = criteriaBuilder.count(studentsJoin.get(Student_.id));
        criteriaQuery.having(criteriaBuilder.le(count, studentsQuantity));

        TypedQuery<Group> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Override
    public Optional<Group> findById(Long id) {
        Group group = em.find(Group.class, id);
        return Optional.ofNullable(group);
    }

    @Override
    public Optional<Group> findByName(String name) {
        Group group;
        try {
            group = em.createQuery(FIND_BY_NAME, Group.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }

        return Optional.of(group);
    }

    @Override
    public Group create(Group group) {
        em.persist(group);
        return group;
    }

    @Override
    public Group update(Group group) {
        em.merge(group);
        return group;
    }

    @Override
    public void deleteById(Long id) {
        Group group = em.getReference(Group.class, id);
        em.remove(group);
    }

    @Override
    public Long count() {
        return em.createQuery(COUNT_QUERY, Long.class)
                .getSingleResult();
    }
}
