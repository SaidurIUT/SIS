package com.iut.sis.repository;

import com.iut.sis.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Optional<Subject> findByCode(String code);
    List<Subject> findByDepartmentId(int departmentId);
    List<Subject> findBySemester(int semester);
    List<Subject> findByDepartmentIdAndSemester(int departmentId, int semester);
}