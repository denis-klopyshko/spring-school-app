package com.example.dao.jpa;

import com.example.dao.CourseDao;
import com.example.entity.Course;
import com.example.entity.Student;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaCourseDao implements CourseDao {
    public static final String FIND_BY_NAME = "select c from Course c where c.name = :name";
    private static final String FIND_ALL_SQL = "select c from Course c";
    private static final String COUNT_QUERY = "select count(c) from Course c";

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Course> findAll() {
        return em.createQuery(FIND_ALL_SQL, Course.class)
                .getResultList();
    }

    @Override
    public List<Course> findAllByStudentId(Long studentId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
        Root<Course> root = criteriaQuery.from(Course.class);
        Join<Course, Student> studentsJoin = root.join("students");
        criteriaQuery.select(root)
                .where(criteriaBuilder.equal(studentsJoin.get("id"), studentId));
        TypedQuery<Course> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Optional<Course> findById(Long id) {
        Course course = em.find(Course.class, id);
        return Optional.ofNullable(course);
    }

    @Override
    public Optional<Course> findByName(String name) {
        Course course = em.createQuery(FIND_BY_NAME, Course.class)
                .setParameter("name", name)
                .getSingleResult();
        return Optional.ofNullable(course);
    }

    @Transactional
    @Override
    public Course create(Course course) {
        em.persist(course);
        return course;
    }

    @Override
    public Course update(Course course) {
        em.merge(course);
        return course;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Course course = em.getReference(Course.class, id);
        em.remove(course);
    }

    @Override
    public Long count() {
        return em.createQuery(COUNT_QUERY, Long.class)
                .getSingleResult();
    }
}
