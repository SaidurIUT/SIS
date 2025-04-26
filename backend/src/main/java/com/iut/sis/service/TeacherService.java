package com.iut.sis.service;

import com.iut.sis.payloads.TeacherInfoDto;
import java.util.List;

public interface TeacherService {
    TeacherInfoDto registerTeacher(TeacherInfoDto teacherInfoDto, Integer userId);
    TeacherInfoDto updateTeacherInfo(TeacherInfoDto teacherInfoDto, Integer teacherId);
    TeacherInfoDto getTeacherById(Integer teacherId);
    List<TeacherInfoDto> getAllTeachers();
    List<TeacherInfoDto> getTeachersByDepartment(Integer departmentId);
    void deleteTeacher(Integer teacherId);
}