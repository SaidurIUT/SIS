package com.iut.sis.service;

import com.iut.sis.payloads.DepartmentDto;
import java.util.List;

public interface DepartmentService {
    DepartmentDto createDepartment(DepartmentDto departmentDto);
    DepartmentDto updateDepartment(DepartmentDto departmentDto, Integer departmentId);
    DepartmentDto getDepartmentById(Integer departmentId);
    DepartmentDto getDepartmentByCode(Integer code);
    List<DepartmentDto> getAllDepartments();
    void deleteDepartment(Integer departmentId);
}