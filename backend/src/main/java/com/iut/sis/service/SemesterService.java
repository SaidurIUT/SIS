package com.iut.sis.service;

import com.iut.sis.payloads.SemesterDto;
import java.util.List;

public interface SemesterService {
    SemesterDto createSemester(SemesterDto semesterDto);
    SemesterDto updateSemester(SemesterDto semesterDto, Integer semesterId);
    SemesterDto getSemesterById(Integer semesterId);
    SemesterDto getCurrentSemester();
    List<SemesterDto> getAllSemesters();
    List<SemesterDto> getSemestersBySession(Integer sessionId);
    void deleteSemester(Integer semesterId);
    void setCurrentSemester(Integer semesterId);
}