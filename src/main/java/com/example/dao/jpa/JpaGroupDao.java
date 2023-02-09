package com.example.dao.jpa;

import com.example.dao.GroupDao;
import com.example.entity.Group;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaGroupDao implements GroupDao {
    public static final String FIND_BY_NAME = "select g from Group g where g.name = :name";
    private static final String FIND_ALL_SQL = "select g from Group g";
    private static final String COUNT_QUERY = "select count(g) from Group g";

    private static final String GET_BY_STUDENTS_COUNT_SQL =
            "SELECT g FROM Group g " +
                    "LEFT JOIN Student s " +
                    "ON g.id = s.group.id " +
                    "GROUP BY g " +
                    "HAVING count(s) <= :studentLimit";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Group> findAll() {
        return em.createQuery(FIND_ALL_SQL, Group.class)
                .getResultList();
    }

    @Override
    public List<Group> findAllWithLessOrEqualStudents(Long studentsQuantity) {
        return em.createQuery(GET_BY_STUDENTS_COUNT_SQL, Group.class)
                .setParameter("studentLimit", studentsQuantity)
                .getResultList();
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
