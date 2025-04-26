package com.iut.sis.service.impl;

import com.iut.sis.entity.Course;
import com.iut.sis.entity.Department;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.CourseDto;
import com.iut.sis.repository.CourseRepository;
import com.iut.sis.repository.DepartmentRepository;
import com.iut.sis.service.CourseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CourseDto createCourse(CourseDto courseDto) {
        Department department = this.departmentRepository.findById(courseDto.getDepartment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", courseDto.getDepartment().getId()));

        Course course = this.modelMapper.map(courseDto, Course.class);
        course.setDepartment(department);

        Course savedCourse = this.courseRepository.save(course);
        return this.modelMapper.map(savedCourse, CourseDto.class);
    }

    @Override
    public CourseDto updateCourse(CourseDto courseDto, Integer courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId));

        if(courseDto.getDepartment() != null) {
            Department department = this.departmentRepository.findById(courseDto.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", courseDto.getDepartment().getId()));
            course.setDepartment(department);
        }

        course.setName(courseDto.getName());
        course.setCode(courseDto.getCode());

        Course updatedCourse = this.courseRepository.save(course);
        return this.modelMapper.map(updatedCourse, CourseDto.class);
    }

    @Override
    public CourseDto getCourseById(Integer courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId));
        return this.modelMapper.map(course, CourseDto.class);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        List<Course> courses = this.courseRepository.findAll();
        return courses.stream()
                .map(course -> this.modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseDto> getCoursesByDepartment(Integer departmentId) {
        List<Course> courses = this.courseRepository.findByDepartmentId(departmentId);
        return courses.stream()
                .map(course -> this.modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCourse(Integer courseId) {
        Course course = this.courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", courseId));
        this.courseRepository.delete(course);
    }
}