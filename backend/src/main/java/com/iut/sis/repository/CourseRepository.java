package com.iut.sis.repository;

import com.iut.sis.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByDepartmentId(int departmentId);
    Course findByNameAndDepartmentId(String name, int departmentId);
    Course findByCodeAndDepartmentId(int code, int departmentId);
}