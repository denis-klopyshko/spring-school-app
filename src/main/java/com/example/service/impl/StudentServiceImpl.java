package com.example.service.impl;

import com.example.dto.CourseDto;
import com.example.dto.GroupDto;
import com.example.dto.StudentDto;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.exception.ConflictException;
import com.example.exception.ResourceNotFoundException;
import com.example.mapping.StudentMapper;
import com.example.repository.CourseRepository;
import com.example.repository.GroupRepository;
import com.example.repository.StudentRepository;
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
    private final StudentRepository studentRepo;

    private final GroupRepository groupRepo;

    private final CourseRepository courseRepo;

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findAll() {
        return studentRepo.findAll()
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findAllByGroupId(Long groupId) {
        return studentRepo.findAllByGroupId(groupId)
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDto> findAllByCourseName(String courseName) {
        return studentRepo.findAllByCourses_Name(courseName)
                .stream()
                .map(MAPPER::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto create(StudentDto studentDto) {
        log.info("Creating student: {}", studentDto);
        var studentEntity = MAPPER.mapBaseAttributes(studentDto);
        setStudentGroup(studentDto, studentEntity);

        var createdStudent = studentRepo.save(studentEntity);

        if (!studentDto.getCourses().isEmpty()) {
            studentDto.getCourses()
                    .forEach(courseDto -> assignStudentOnCourse(createdStudent, courseDto));
        }

        return MAPPER.mapToDto(createdStudent);
    }

    private void setStudentGroup(StudentDto studentDto, Student studentEntity) {
        Optional.ofNullable(studentDto.getGroup())
                .map(GroupDto::getId)
                .flatMap(groupRepo::findById)
                .ifPresent(studentEntity::setGroup);
    }

    @Override
    public StudentDto update(Long id, StudentDto studentDto) {
        log.info("Updating student with ID [{}]. Payload: {}", id, studentDto);
        var student = findStudentEntity(id);

        MAPPER.updateStudentFromDto(studentDto, student);
        var updatedStudent = studentRepo.save(student);

        if (!studentDto.getCourses().isEmpty()) {
            student.getCourses().clear();
            studentDto.getCourses()
                    .forEach(courseDto -> assignStudentOnCourse(updatedStudent, courseDto));
        }

        return MAPPER.mapToDto(updatedStudent);
    }

    @Transactional(readOnly = true)
    @Override
    public StudentDto findOne(Long id) {
        var studentEntity = findStudentEntity(id);
        return MAPPER.mapToDto(studentEntity);
    }

    @Override
    public void delete(Long studentId) {
        log.info("Deleting student with id: [{}]", studentId);
        findStudentEntity(studentId);
        studentRepo.deleteById(studentId);
    }

    @Override
    public void assignStudentOnCourse(Long studentId, Long courseId) {
        var studentEntity = findStudentEntity(studentId);
        assignStudentOnCourse(studentEntity, CourseDto.ofId(courseId));
    }

    @Override
    public void removeStudentFromCourse(Long studentId, Long courseId) {
        var studentEntity = findStudentEntity(studentId);
        removeStudentFromCourse(studentEntity, CourseDto.ofId(courseId));
    }

    private void assignStudentOnCourse(Student student, CourseDto courseDto) {
        log.info("Assigning student with ID [{}] on course: {}", student.getId(), courseDto);
        Course course = findCourseEntity(courseDto.getId());

        if(student.getCourses().contains(course)) {
            log.error("Student with ID:[{}] already assigned on course with ID:[{}]", student.getId(), courseDto);
            throw new ConflictException(
                    format("Student with ID:[%s] already assigned on course with ID: [%s]", student.getId(), courseDto.getId())
            );
        }

        student.addCourse(course);
    }

    private void removeStudentFromCourse(Student student, CourseDto courseDto) {
        log.info("Removing student with ID [{}] from course: {}", student.getId(), courseDto);
        Course course = findCourseEntity(courseDto.getId());

        if (!student.getCourses().contains(course)) {
            log.error("Student with ID:[{}] NOT assigned on course with ID:[{}]", student.getId(), courseDto);
            throw new IllegalStateException(
                    format("Student with ID:[%s] NOT assigned on course with ID: [%s]", student.getId(), courseDto.getId())
            );
        }

        student.removeCourse(course);
    }

    private Student findStudentEntity(Long id) {
        return studentRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(format("Student with id: %s not found!", id))
                );
    }

    private Course findCourseEntity(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(format("Course with id: %s not found!", id))
                );
    }
}
