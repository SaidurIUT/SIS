package com.iut.sis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects")
@NoArgsConstructor
@Getter
@Setter
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code; // Format: CSE 4145

    @Column(name = "credit_hours")
    private double creditHours;

    @Column(name = "semester")
    private int semester;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany(mappedBy = "courseSubjects")
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "enrolledSubjects")
    private Set<StudentInfo> enrolledStudents = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "subject_teacher",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<TeacherInfo> assignedTeachers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "session_subject",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "session_id")
    )
    private Set<EducationalSession> sessions = new HashSet<>();
}