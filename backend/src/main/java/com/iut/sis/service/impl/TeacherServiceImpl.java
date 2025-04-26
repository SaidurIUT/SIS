package com.iut.sis.service.impl;

import com.iut.sis.entity.Department;
import com.iut.sis.entity.TeacherInfo;
import com.iut.sis.entity.User;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.TeacherInfoDto;
import com.iut.sis.repository.DepartmentRepository;
import com.iut.sis.repository.TeacherInfoRepository;
import com.iut.sis.repository.UserRepo;
import com.iut.sis.service.TeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherInfoRepository teacherRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TeacherInfoDto registerTeacher(TeacherInfoDto teacherInfoDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));

        TeacherInfo teacherInfo = this.modelMapper.map(teacherInfoDto, TeacherInfo.class);

        teacherInfo.setUser(user);

        TeacherInfo savedTeacher = this.teacherRepository.save(teacherInfo);

        return this.modelMapper.map(savedTeacher, TeacherInfoDto.class);
    }

    @Override
    public TeacherInfoDto updateTeacherInfo(TeacherInfoDto teacherInfoDto, Integer teacherId) {
        TeacherInfo teacherInfo = this.teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "Id", teacherId));

        if (teacherInfoDto.getDepartment().getId() != 0) {
            Department department = this.departmentRepository.findById(teacherInfoDto.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", teacherInfoDto.getDepartment().getId()));
            teacherInfo.setDepartment(department);
        }

        if (teacherInfoDto.getDesignation() != null) {
            teacherInfo.setDesignation(teacherInfoDto.getDesignation());
        }

        if (teacherInfoDto.getJoiningDate() != null) {
            teacherInfo.setJoiningDate(teacherInfoDto.getJoiningDate());
        }

        TeacherInfo updatedTeacher = this.teacherRepository.save(teacherInfo);
        return this.modelMapper.map(updatedTeacher, TeacherInfoDto.class);
    }

    @Override
    public TeacherInfoDto getTeacherById(Integer teacherId) {
        TeacherInfo teacherInfo = this.teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "Id", teacherId));
        return this.modelMapper.map(teacherInfo, TeacherInfoDto.class);
    }

    @Override
    public List<TeacherInfoDto> getAllTeachers() {
        List<TeacherInfo> teachers = this.teacherRepository.findAll();
        return teachers.stream()
                .map(teacher -> this.modelMapper.map(teacher, TeacherInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherInfoDto> getTeachersByDepartment(Integer departmentId) {
        List<TeacherInfo> teachers = this.teacherRepository.findByDepartmentId(departmentId);
        return teachers.stream()
                .map(teacher -> this.modelMapper.map(teacher, TeacherInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTeacher(Integer teacherId) {
        TeacherInfo teacherInfo = this.teacherRepository.findById(teacherId)
                .orElseThrow(() -> new ResourceNotFoundException("Teacher", "Id", teacherId));
        this.teacherRepository.delete(teacherInfo);
    }
}