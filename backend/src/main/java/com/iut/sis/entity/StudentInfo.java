package com.iut.sis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student_info")
@NoArgsConstructor
@Getter
@Setter
public class StudentInfo {
    @Id
    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId; // Format: 210041132

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "batch")
    private String batch; // HSC batch (first 2 digits of studentId)

    @Column(name = "section")
    private int section;

    @Column(name = "roll")
    private int roll;

    @Column(name = "current_semester")
    private int currentSemester;

    @ManyToMany
    @JoinTable(
            name = "student_subject",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> enrolledSubjects = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "student_session",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "session_id")
    )
    private Set<EducationalSession> sessions = new HashSet<>();
}