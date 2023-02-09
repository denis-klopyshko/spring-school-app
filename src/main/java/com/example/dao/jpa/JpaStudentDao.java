package com.example.dao.jpa;

import com.example.dao.StudentDao;
import com.example.entity.Course;
import com.example.entity.Group;
import com.example.entity.Student;
import com.example.entity.Student_;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaStudentDao implements StudentDao {
    private static final String FIND_ALL_SQL = "select s from Student s";
    private static final String COUNT_QUERY = "select count(g) from Group g";
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Student> findAll() {
        return em.createQuery(FIND_ALL_SQL, Student.class)
                .getResultList();
    }

    @Override
    public List<Student> findAllByGroupId(Long groupId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
        Root<Student> studentRoot = criteriaQuery.from(Student.class);
        Join<Student, Group> groupJoin = studentRoot.join("group");
        criteriaQuery.select(studentRoot)
                .where(criteriaBuilder.equal(groupJoin.get("id"), groupId));
        TypedQuery<Student> query = em.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public List<Student> findAllByCourseName(String courseName) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Student> criteriaQuery = criteriaBuilder.createQuery(Student.class);
        Root<Student> studentRoot = criteriaQuery.from(Student.class);
        Join<Student, Course> coursesJoin = studentRoot.join("courses");
        criteriaQuery.select(studentRoot)
                .where(criteriaBuilder.equal(coursesJoin.get("name"), courseName));
        TypedQuery<Student> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Optional<Student> findById(Long id) {
        Student student = em.find(Student.class, id);
        return Optional.ofNullable(student);
    }

    @Override
    public Optional<Student> findByFirstNameAndLastName(String firstName, String lastName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Student> q = cb.createQuery(Student.class);
        Root<Student> studentRoot = q.from(Student.class);
        q.select(studentRoot).where(cb.and(
                cb.equal(studentRoot.get(Student_.firstName), firstName),
                cb.equal(studentRoot.get(Student_.lastName), lastName)
        ));
        TypedQuery<Student> query = em.createQuery(q);

        Student student = null;
        try {
            student = query.getSingleResult();
        } catch (NoResultException e) {
            return Optional.empty();
        }

        return Optional.of(student);
    }

    @Override
    public Student create(Student student) {
        em.persist(student);
        return student;
    }

    @Override
    public Student update(Student student) {
        em.merge(student);
        return student;
    }

    @Override
    public void deleteById(Long id) {
        Student student = em.getReference(Student.class, id);
        em.remove(student);
    }

    @Override
    public boolean assignStudentOnCourse(Long studentId, Long courseId) {
        String insertQuery = "INSERT INTO students_courses (student_id, course_id) VALUES (:studentId, :courseId)";

        Query query = em.createNativeQuery(insertQuery);
        query.setParameter("studentId", studentId);
        query.setParameter("courseId", courseId);

        return query.executeUpdate() == 1;
    }

    @Override
    public void removeStudentCourses(Long studentId) {
        String insertQuery = "DELETE FROM students_courses WHERE student_id= :studentId";

        Query query = em.createNativeQuery(insertQuery);
        query.setParameter("studentId", studentId);
        query.executeUpdate();
    }

    @Override
    public Long count() {
        return em.createQuery(COUNT_QUERY, Long.class)
                .getSingleResult();
    }
}
