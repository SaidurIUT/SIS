package com.iut.sis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "semesters")
@NoArgsConstructor
@Getter
@Setter
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", nullable = false)
    private String name; // e.g., "First Semester", "Second Semester"

    @Column(name = "semester_number")
    private int semesterNumber; // 1 to 8

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_current")
    private boolean isCurrent;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private EducationalSession session;

    @ManyToMany
    @JoinTable(
            name = "semester_subject",
            joinColumns = @JoinColumn(name = "semester_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> offeredSubjects = new HashSet<>();
}
