package com.iut.sis.repository;

import com.iut.sis.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department findByCode(int code);
    Department findByName(String name);
}