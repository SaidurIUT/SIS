package com.iut.sis.service.impl;

import com.iut.sis.entity.Department;
import com.iut.sis.entity.Subject;
import com.iut.sis.exception.ApiException;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.SubjectDto;
import com.iut.sis.repository.DepartmentRepository;
import com.iut.sis.repository.SubjectRepository;
import com.iut.sis.service.SubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SubjectDto createSubject(SubjectDto subjectDto) {
        Department department = this.departmentRepository.findById(subjectDto.getDepartment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", subjectDto.getDepartment().getId()));

        // Validate subject code format: [DeptName] [DeptCode][Semester][ID]
        String codePattern = department.getName() + " " + department.getCode() + subjectDto.getSemester();
        if (!subjectDto.getCode().startsWith(codePattern)) {
            throw new ApiException("Subject code must follow format: [DeptName] [DeptCode][Semester][ID]");
        }

        Subject subject = this.modelMapper.map(subjectDto, Subject.class);
        subject.setDepartment(department);

        Subject savedSubject = this.subjectRepository.save(subject);
        return this.modelMapper.map(savedSubject, SubjectDto.class);
    }

    @Override
    public SubjectDto updateSubject(SubjectDto subjectDto, Integer subjectId) {
        Subject subject = this.subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "Id", subjectId));

        if (subjectDto.getName() != null) {
            subject.setName(subjectDto.getName());
        }

        if (subjectDto.getCode() != null) {
            subject.setCode(subjectDto.getCode());
        }

        if (subjectDto.getCreditHours() != 0) {
            subject.setCreditHours(subjectDto.getCreditHours());
        }

        if (subjectDto.getSemester() != 0) {
            subject.setSemester(subjectDto.getSemester());
        }

        if (subjectDto.getDepartment().getId() != 0) {
            Department department = this.departmentRepository.findById(subjectDto.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", subjectDto.getDepartment().getId()));
            subject.setDepartment(department);
        }

        Subject updatedSubject = this.subjectRepository.save(subject);
        return this.modelMapper.map(updatedSubject, SubjectDto.class);
    }

    @Override
    public SubjectDto getSubjectById(Integer subjectId) {
        Subject subject = this.subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "Id", subjectId));
        return this.modelMapper.map(subject, SubjectDto.class);
    }

    @Override
    public SubjectDto getSubjectByCode(String code) {
        Subject subject = this.subjectRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "Code", code));
        return this.modelMapper.map(subject, SubjectDto.class);
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        List<Subject> subjects = this.subjectRepository.findAll();
        return subjects.stream()
                .map(subject -> this.modelMapper.map(subject, SubjectDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> getSubjectsByDepartment(Integer departmentId) {
        List<Subject> subjects = this.subjectRepository.findByDepartmentId(departmentId);
        return subjects.stream()
                .map(subject -> this.modelMapper.map(subject, SubjectDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> getSubjectsBySemester(Integer semester) {
        List<Subject> subjects = this.subjectRepository.findBySemester(semester);
        return subjects.stream()
                .map(subject -> this.modelMapper.map(subject, SubjectDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDto> getSubjectsByDepartmentAndSemester(Integer departmentId, Integer semester) {
        List<Subject> subjects = this.subjectRepository.findByDepartmentIdAndSemester(departmentId, semester);
        return subjects.stream()
                .map(subject -> this.modelMapper.map(subject, SubjectDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSubject(Integer subjectId) {
        Subject subject = this.subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "Id", subjectId));
        this.subjectRepository.delete(subject);
    }
}