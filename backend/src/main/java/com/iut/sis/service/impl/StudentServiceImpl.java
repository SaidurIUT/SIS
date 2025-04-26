package com.iut.sis.service.impl;

import com.iut.sis.entity.*;
import com.iut.sis.exception.ApiException;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.StudentInfoDto;
import com.iut.sis.repository.*;
import com.iut.sis.service.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentInfoRepository studentRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public StudentInfoDto registerStudent(StudentInfoDto studentInfoDto, Integer userId) {
        User user = this.userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));



        StudentInfo studentInfo = this.modelMapper.map(studentInfoDto, StudentInfo.class);
        studentInfo.setUser(user);
        studentInfo.setStudentId(UUID.randomUUID().toString());




        studentInfo.setCurrentSemester(1); // Start with semester 1

        StudentInfo savedStudent = this.studentRepository.save(studentInfo);
        return this.modelMapper.map(savedStudent, StudentInfoDto.class);
    }

    @Override
    public StudentInfoDto setStudentInfo(StudentInfoDto studentInfoDto, int userId) {

        Department department = this.departmentRepository.findById(studentInfoDto.getDepartment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "Id", studentInfoDto.getDepartment().getId()));

        Course course = this.courseRepository.findById(studentInfoDto.getCourse().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "Id", studentInfoDto.getCourse().getId()));

//        // Check if course belongs to department
        if (course.getDepartment().getId() != department.getId()) {
            throw new ApiException("Course does not belong to the specified department");
        }


        StudentInfo studentInfo = this.studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "User Id", userId));

        studentInfo.setDepartment(department);
        studentInfo.setCourse(course);
        studentInfo.setBatch(studentInfoDto.getBatch());
        studentInfo.setSection(studentInfoDto.getSection());

        // Auto-generate roll number based on batch, dept, course, section
        Optional<Integer> maxRoll = studentRepository.findMaxRollNumber(
                studentInfoDto.getBatch(),
                department.getId(),
                course.getCode(),
                studentInfoDto.getSection());

        int nextRoll = maxRoll.orElse(0) + 1;
        studentInfo.setRoll(nextRoll);



        // Generate student ID

        // take lsb 2 digits of batch
        String batchCode = studentInfoDto.getBatch().substring(2);
        String deptCode = String.valueOf(department.getCode());
        String courseCode = String.valueOf(course.getCode());
        String sectionCode = String.valueOf(studentInfoDto.getSection());
        String rollCode = String.format("%02d", nextRoll);

        String studentId = batchCode + "00" + deptCode + courseCode + sectionCode + rollCode;

        studentInfo.setStudentId(studentId);



        StudentInfo savedStudent = this.studentRepository.save(studentInfo);
        return this.modelMapper.map(savedStudent, StudentInfoDto.class);


    }

    @Override
    public StudentInfoDto updateStudentInfo(StudentInfoDto studentInfoDto, String studentId) {
        StudentInfo studentInfo = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));

        if (studentInfoDto.getCurrentSemester() != 0) {
            studentInfo.setCurrentSemester(studentInfoDto.getCurrentSemester());
        }

        if (studentInfoDto.getSection() != 0) {
            studentInfo.setSection(studentInfoDto.getSection());
        }

        StudentInfo updatedStudent = this.studentRepository.save(studentInfo);
        return this.modelMapper.map(updatedStudent, StudentInfoDto.class);
    }

    @Override
    public StudentInfoDto getStudentById(String studentId) {
        StudentInfo studentInfo = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
        return this.modelMapper.map(studentInfo, StudentInfoDto.class);
    }

    @Override
    public List<StudentInfoDto> getAllStudents() {
        List<StudentInfo> students = this.studentRepository.findAll();
        return students.stream()
                .map(student -> this.modelMapper.map(student, StudentInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentInfoDto> getStudentsByDepartment(Integer departmentId) {
        List<StudentInfo> students = this.studentRepository.findByDepartmentId(departmentId);
        return students.stream()
                .map(student -> this.modelMapper.map(student, StudentInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentInfoDto> getStudentsByCourse(Integer courseId) {
        List<StudentInfo> students = this.studentRepository.findByCourseId(courseId);
        return students.stream()
                .map(student -> this.modelMapper.map(student, StudentInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentInfoDto> getStudentsByBatch(String batch) {
        List<StudentInfo> students = this.studentRepository.findByBatch(batch);
        return students.stream()
                .map(student -> this.modelMapper.map(student, StudentInfoDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteStudent(String studentId) {
        StudentInfo studentInfo = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));
        this.studentRepository.delete(studentInfo);
    }

    @Override
    public void updateSemester(String studentId, Integer newSemester) {
        StudentInfo studentInfo = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));

        if (newSemester < 1 || newSemester > 8) {
            throw new ApiException("Semester must be between 1 and 8");
        }

        studentInfo.setCurrentSemester(newSemester);
        this.studentRepository.save(studentInfo);
    }

    @Override
    public List<StudentInfoDto> getStudentsByCurrentSemester(Integer semester) {
        List<StudentInfo> students = this.studentRepository.findByCurrentSemester(semester);
        return students.stream()
                .map(student -> this.modelMapper.map(student, StudentInfoDto.class))
                .collect(Collectors.toList());
    }
}