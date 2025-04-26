package com.iut.sis.service;

import com.iut.sis.payloads.StudentInfoDto;
import java.util.List;

public interface StudentService {
    StudentInfoDto registerStudent(StudentInfoDto studentInfoDto, Integer userId);
    StudentInfoDto setStudentInfo(StudentInfoDto studentInfoDto, int userId);
    StudentInfoDto updateStudentInfo(StudentInfoDto studentInfoDto, String studentId);
    StudentInfoDto getStudentById(String studentId);
    List<StudentInfoDto> getAllStudents();
    List<StudentInfoDto> getStudentsByDepartment(Integer departmentId);
    List<StudentInfoDto> getStudentsByCourse(Integer courseId);
    List<StudentInfoDto> getStudentsByBatch(String batch);
    void deleteStudent(String studentId);
    void updateSemester(String studentId, Integer newSemester);
    List<StudentInfoDto> getStudentsByCurrentSemester(Integer semester);
}