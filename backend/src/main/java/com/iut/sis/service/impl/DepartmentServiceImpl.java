package com.iut.sis.service.impl;

import com.iut.sis.entity.Department;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.DepartmentDto;
import com.iut.sis.repository.DepartmentRepository;
import com.iut.sis.service.DepartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DepartmentDto createDepartment(DepartmentDto departmentDto) {
        Department department = this.modelMapper.map(departmentDto, Department.class);
        Department savedDepartment = this.departmentRepository.save(department);
        return this.modelMapper.map(savedDepartment, DepartmentDto.class);
    }

    @Override
    public DepartmentDto updateDepartment(DepartmentDto departmentDto, Integer departmentId) {
        Department department = this.departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", departmentId));

        department.setName(departmentDto.getName());
        department.setCode(departmentDto.getCode());

        Department updatedDepartment = this.departmentRepository.save(department);
        return this.modelMapper.map(updatedDepartment, DepartmentDto.class);
    }

    @Override
    public DepartmentDto getDepartmentById(Integer departmentId) {
        Department department = this.departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", departmentId));
        return this.modelMapper.map(department, DepartmentDto.class);
    }

    @Override
    public DepartmentDto getDepartmentByCode(Integer code) {
        Department department = this.departmentRepository.findByCode(code);
        if (department == null) {
            throw new ResourceNotFoundException("Department", "Code", code);
        }
        return this.modelMapper.map(department, DepartmentDto.class);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        List<Department> departments = this.departmentRepository.findAll();
        return departments.stream()
                .map(department -> this.modelMapper.map(department, DepartmentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDepartment(Integer departmentId) {
        Department department = this.departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", departmentId));
        this.departmentRepository.delete(department);
    }
}