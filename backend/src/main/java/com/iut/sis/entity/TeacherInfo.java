package com.iut.sis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "teacher_info")
@NoArgsConstructor
@Getter
@Setter
public class TeacherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "designation")
    private String designation;

    @Column(name = "joining_date")
    private String joiningDate;

    @ManyToMany(mappedBy = "assignedTeachers")
    private Set<Subject> teachingSubjects = new HashSet<>();
}