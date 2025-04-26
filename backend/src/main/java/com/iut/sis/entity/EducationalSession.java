package com.iut.sis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "educational_sessions")
@NoArgsConstructor
@Getter
@Setter
public class EducationalSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "name", nullable = false)
    private String name; // e.g., "Fall 2024", "Spring 2025"

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_current")
    private boolean isCurrent;

    @ManyToMany(mappedBy = "sessions")
    private Set<Subject> subjects = new HashSet<>();

    @ManyToMany(mappedBy = "sessions")
    private Set<StudentInfo> students = new HashSet<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Semester> semesters = new HashSet<>();
}