package com.iut.sis.service.impl;

import com.iut.sis.entity.Semester;
import com.iut.sis.entity.StudentInfo;
import com.iut.sis.entity.Subject;
import com.iut.sis.entity.SubjectRegistration;
import com.iut.sis.exception.ApiException;
import com.iut.sis.exception.ResourceNotFoundException;
import com.iut.sis.payloads.SubjectRegistrationDto;
import com.iut.sis.repository.SemesterRepository;
import com.iut.sis.repository.StudentInfoRepository;
import com.iut.sis.repository.SubjectRegistrationRepository;
import com.iut.sis.repository.SubjectRepository;
import com.iut.sis.service.SubjectRegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectRegistrationServiceImpl implements SubjectRegistrationService {

    @Autowired
    private SubjectRegistrationRepository registrationRepository;

    @Autowired
    private StudentInfoRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public SubjectRegistrationDto registerSubject(SubjectRegistrationDto registrationDto) {
        // Get student
        StudentInfo student = this.studentRepository.findById(registrationDto.getStudent().getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", registrationDto.getStudent().getStudentId()));

        // Get subject
        Subject subject = this.subjectRepository.findById(registrationDto.getSubject().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "Id", registrationDto.getSubject().getId()));

        // Get semester
        Semester semester = this.semesterRepository.findById(registrationDto.getSemester().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "Id", registrationDto.getSemester().getId()));

        // Check if subject is offered in this semester
        if (!semester.getOfferedSubjects().contains(subject)) {
            throw new ApiException("Subject " + subject.getCode() + " is not offered in this semester");
        }

        // Check if subject is appropriate for student's current semester
        if (subject.getSemester() != student.getCurrentSemester()) {
            throw new ApiException("Subject " + subject.getCode() + " is not appropriate for student's current semester");
        }

        // Check if student has already registered for this subject in this semester
        boolean alreadyRegistered = registrationRepository.existsByStudentAndSubjectAndSemester(student, subject, semester);
        if (alreadyRegistered) {
            throw new ApiException("Student has already registered for this subject in this semester");
        }

        // Create and save registration
        SubjectRegistration registration = new SubjectRegistration();
        registration.setStudent(student);
        registration.setSubject(subject);
        registration.setSemester(semester);
        registration.setRegistrationDate(LocalDate.now());
        registration.setActive(true);

        // Add subject to student's enrolled subjects
        student.getEnrolledSubjects().add(subject);
        studentRepository.save(student);

        SubjectRegistration savedRegistration = this.registrationRepository.save(registration);
        return this.modelMapper.map(savedRegistration, SubjectRegistrationDto.class);
    }

    @Override
    public SubjectRegistrationDto updateRegistration(SubjectRegistrationDto registrationDto, Integer registrationId) {
        SubjectRegistration registration = this.registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject Registration", "Id", registrationId));

        if (registrationDto.isActive() != registration.isActive()) {
            registration.setActive(registrationDto.isActive());
        }

        if (registrationDto.getGrade() != null && !registrationDto.getGrade().equals(registration.getGrade())) {
            registration.setGrade(registrationDto.getGrade());
        }

        SubjectRegistration updatedRegistration = this.registrationRepository.save(registration);
        return this.modelMapper.map(updatedRegistration, SubjectRegistrationDto.class);
    }

    @Override
    public SubjectRegistrationDto getRegistrationById(Integer registrationId) {
        SubjectRegistration registration = this.registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject Registration", "Id", registrationId));
        return this.modelMapper.map(registration, SubjectRegistrationDto.class);
    }

    @Override
    public List<SubjectRegistrationDto> getRegistrationsByStudent(String studentId) {
        StudentInfo student = this.studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "Id", studentId));

        List<SubjectRegistration> registrations = this.registrationRepository.findByStudent(student);
        return registrations.stream()
                .map(registration -> this.modelMapper.map(registration, SubjectRegistrationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectRegistrationDto> getRegistrationsBySubject(Integer subjectId) {
        Subject subject = this.subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject", "Id", subjectId));

        List<SubjectRegistration> registrations = this.registrationRepository.findBySubject(subject);
        return registrations.stream()
                .map(registration -> this.modelMapper.map(registration, SubjectRegistrationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectRegistrationDto> getRegistrationsBySemester(Integer semesterId) {
        Semester semester = this.semesterRepository.findById(semesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Semester", "Id", semesterId));

        List<SubjectRegistration> registrations = this.registrationRepository.findBySemester(semester);
        return registrations.stream()
                .map(registration -> this.modelMapper.map(registration, SubjectRegistrationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectRegistrationDto> getActiveRegistrations() {
        List<SubjectRegistration> registrations = this.registrationRepository.findByIsActive(true);
        return registrations.stream()
                .map(registration -> this.modelMapper.map(registration, SubjectRegistrationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRegistration(Integer registrationId) {
        SubjectRegistration registration = this.registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject Registration", "Id", registrationId));

        // Remove subject from student's enrolled subjects
        StudentInfo student = registration.getStudent();
        Subject subject = registration.getSubject();
        student.getEnrolledSubjects().remove(subject);
        studentRepository.save(student);

        this.registrationRepository.delete(registration);
    }

    @Override
    public void assignGrade(Integer registrationId, String grade) {
        SubjectRegistration registration = this.registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject Registration", "Id", registrationId));

        registration.setGrade(grade);
        this.registrationRepository.save(registration);
    }
}