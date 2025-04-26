package com.iut.sis.repository;

import com.iut.sis.entity.TeacherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherInfoRepository extends JpaRepository<TeacherInfo, Integer> {
    List<TeacherInfo> findByDepartmentId(int departmentId);
    List<TeacherInfo> findByDesignation(String designation);
}