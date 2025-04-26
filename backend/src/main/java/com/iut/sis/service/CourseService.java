package com.iut.sis.service;

import com.iut.sis.payloads.CourseDto;
import java.util.List;

public interface CourseService {
    CourseDto createCourse(CourseDto courseDto);
    CourseDto updateCourse(CourseDto courseDto, Integer courseId);
    CourseDto getCourseById(Integer courseId);
    List<CourseDto> getAllCourses();
    List<CourseDto> getCoursesByDepartment(Integer departmentId);
    void deleteCourse(Integer courseId);
}