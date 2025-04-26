package com.iut.sis.repository;

import com.iut.sis.entity.Semester;
import com.iut.sis.entity.StudentInfo;
import com.iut.sis.entity.Subject;
import com.iut.sis.entity.SubjectRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRegistrationRepository  extends JpaRepository<SubjectRegistration, Integer> {
    List<SubjectRegistration> findByStudent(StudentInfo student);
    List<SubjectRegistration> findBySubject(Subject subject);
    List<SubjectRegistration> findBySemester(Semester semester);
    List<SubjectRegistration> findByIsActive(boolean isActive);
    boolean existsByStudentAndSubjectAndSemester(StudentInfo student, Subject subject, Semester semester);
}
