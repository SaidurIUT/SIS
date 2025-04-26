package com.iut.sis.controller;

import com.iut.sis.payloads.ApiResponse;
import com.iut.sis.payloads.StudentInfoDto;
import com.iut.sis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studentInfo")
public class StudentInfoController {

    @Autowired
    private StudentService studentService;

    // Register a new student
//    @PostMapping("/register/{userId}")
//    public ResponseEntity<StudentInfoDto> registerStudent(
//            @RequestBody StudentInfoDto studentInfoDto,
//            @PathVariable Integer userId) {
//        StudentInfoDto registeredStudent = this.studentService.registerStudent(studentInfoDto, userId);
//        return new ResponseEntity<>(registeredStudent, HttpStatus.CREATED);
//    }

    // Set student information (department, course, batch, section)
    @PutMapping("/setInfo/{userId}")
    public ResponseEntity<StudentInfoDto> setStudentInfo(
            @RequestBody StudentInfoDto studentInfoDto,
            @PathVariable Integer userId) {
        StudentInfoDto updatedStudent = this.studentService.setStudentInfo(studentInfoDto, userId);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    // Update student information
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentInfoDto> updateStudentInfo(
            @RequestBody StudentInfoDto studentInfoDto,
            @PathVariable String studentId) {
        StudentInfoDto updatedStudent = this.studentService.updateStudentInfo(studentInfoDto, studentId);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    // Get student by ID
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentInfoDto> getStudentById(@PathVariable String studentId) {
        StudentInfoDto student = this.studentService.getStudentById(studentId);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<StudentInfoDto>> getAllStudents() {
        List<StudentInfoDto> students = this.studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get students by department
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<StudentInfoDto>> getStudentsByDepartment(@PathVariable Integer departmentId) {
        List<StudentInfoDto> students = this.studentService.getStudentsByDepartment(departmentId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get students by course
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<StudentInfoDto>> getStudentsByCourse(@PathVariable Integer courseId) {
        List<StudentInfoDto> students = this.studentService.getStudentsByCourse(courseId);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get students by batch
    @GetMapping("/batch/{batch}")
    public ResponseEntity<List<StudentInfoDto>> getStudentsByBatch(@PathVariable String batch) {
        List<StudentInfoDto> students = this.studentService.getStudentsByBatch(batch);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get students by semester
    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<StudentInfoDto>> getStudentsByCurrentSemester(@PathVariable Integer semester) {
        List<StudentInfoDto> students = this.studentService.getStudentsByCurrentSemester(semester);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Update student semester
    @PatchMapping("/{studentId}/semester/{newSemester}")
    public ResponseEntity<ApiResponse> updateSemester(
            @PathVariable String studentId,
            @PathVariable Integer newSemester) {
        this.studentService.updateSemester(studentId, newSemester);
        return new ResponseEntity<>(
                new ApiResponse("Student semester updated successfully", true),
                HttpStatus.OK);
    }

    // Delete student
    @DeleteMapping("/{studentId}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable String studentId) {
        this.studentService.deleteStudent(studentId);
        return new ResponseEntity<>(
                new ApiResponse("Student deleted successfully", true),
                HttpStatus.OK);
    }
}