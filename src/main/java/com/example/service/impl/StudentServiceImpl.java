package com.example.service.impl;

import com.example.dao.CourseDao;
import com.example.dao.GroupDao;
import com.example.dao.StudentDao;
import com.example.dto.CourseDto;
import com.example.dto.GroupDto;
import com.example.dto.StudentDto;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.exception.ConflictException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapping.StudentMapper;
import com.example.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final StudentMapper MAPPER = StudentMapper.INSTANCE;
    private final StudentDao studentDao;

    private final GroupDao groupDao;

    private final CourseDao courseDao;

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findAll() {
        return studentDao.findAll()
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findAllByGroupId(Long groupId) {
        return studentDao.findAllByGroupId(groupId)
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto create(StudentDto studentDto) {
        log.info("Creating student: {}", studentDto);
        var studentEntity = MAPPER.mapBaseAttributes(studentDto);
        setStudentGroup(studentDto, studentEntity);

        var createdStudent = studentDao.create(studentEntity);

        if (!studentDto.getCourses().isEmpty()) {
            studentDto.getCourses()
                    .forEach(courseDto -> assignStudentOnCourse(createdStudent, courseDto));
        }

        return MAPPER.mapToDto(createdStudent);
    }

    private void setStudentGroup(StudentDto studentDto, Student studentEntity) {
        Optional.ofNullable(studentDto.getGroup())
                .map(GroupDto::getId)
                .flatMap(groupDao::findById)
                .ifPresent(studentEntity::setGroup);
    }

    @Override
    public StudentDto update(Long id, StudentDto studentDto) {
        log.info("Updating student with ID [{}]. Payload: {}", id, studentDto);
        var student = findStudentEntity(id);

        MAPPER.updateStudentFromDto(studentDto, student);
        var updatedStudent = studentDao.update(student);

        if (!studentDto.getCourses().isEmpty()) {
            studentDao.removeStudentCourses(updatedStudent.getId());
            studentDto.getCourses()
                    .forEach(courseDto -> assignStudentOnCourse(updatedStudent, courseDto));
        }

        return MAPPER.mapToDto(updatedStudent);
    }

    @Override
    public StudentDto findOne(Long id) {
        var studentEntity = findStudentEntity(id);
        return MAPPER.mapToDto(studentEntity);
    }

    @Override
    public void delete(Long studentId) {
        log.info("Deleting student with id: [{}]", studentId);
        findStudentEntity(studentId);
        studentDao.deleteById(studentId);
    }

    private void assignStudentOnCourse(Student student, CourseDto courseDto) {
        log.info("Assigning student with ID [{}] on course: {}", student.getId(), courseDto);
        Course course = courseDao.findById(courseDto.getId()).orElseThrow();

        var studentCourses = courseDao.findAllByStudentId(student.getId());
        boolean alreadyAssigned = studentCourses.stream().anyMatch(c -> c.getId().equals(courseDto.getId()));
        if (alreadyAssigned) {
            log.error("Student with ID:[{}] already assigned on course with ID:[{}]", student.getId(), courseDto);
            throw new ConflictException(
                    format("Student with ID:[%s] already assigned on course with ID: [%s]", student.getId(), courseDto.getId())
            );
        }

        studentDao.assignStudentOnCourse(student.getId(), course.getId());
        student.getCourses().add(course);
    }

    private Student findStudentEntity(Long id) {
        return studentDao.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(format("Student with id: %s not found!", id))
                );
    }
}
