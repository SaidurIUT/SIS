package com.iut.sis.service;

import com.iut.sis.payloads.SubjectDto;
import java.util.List;

public interface SubjectService {
    SubjectDto createSubject(SubjectDto subjectDto);
    SubjectDto updateSubject(SubjectDto subjectDto, Integer subjectId);
    SubjectDto getSubjectById(Integer subjectId);
    SubjectDto getSubjectByCode(String code);
    List<SubjectDto> getAllSubjects();
    List<SubjectDto> getSubjectsByDepartment(Integer departmentId);
    List<SubjectDto> getSubjectsBySemester(Integer semester);
    List<SubjectDto> getSubjectsByDepartmentAndSemester(Integer departmentId, Integer semester);
    void deleteSubject(Integer subjectId);
}