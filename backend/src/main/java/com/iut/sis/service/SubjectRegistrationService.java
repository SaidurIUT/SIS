package com.iut.sis.service;

import com.iut.sis.payloads.SubjectRegistrationDto;
import java.util.List;

public interface SubjectRegistrationService {
    SubjectRegistrationDto registerSubject(SubjectRegistrationDto registrationDto);
    SubjectRegistrationDto updateRegistration(SubjectRegistrationDto registrationDto, Integer registrationId);
    SubjectRegistrationDto getRegistrationById(Integer registrationId);
    List<SubjectRegistrationDto> getRegistrationsByStudent(String studentId);
    List<SubjectRegistrationDto> getRegistrationsBySubject(Integer subjectId);
    List<SubjectRegistrationDto> getRegistrationsBySemester(Integer semesterId);
    List<SubjectRegistrationDto> getActiveRegistrations();
    void deleteRegistration(Integer registrationId);
    void assignGrade(Integer registrationId, String grade);
}