package com.iut.sis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "subject_registrations")
@NoArgsConstructor
@Getter
@Setter
public class SubjectRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentInfo student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "grade")
    private String grade;

    @Column(name = "is_active")
    private boolean isActive;
}