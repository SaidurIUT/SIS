package com.iut.sis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class SubjectRegistrationDto {
    private int id;
    private StudentInfoDto student;
    private SubjectDto subject;
    private SemesterDto semester;
    private LocalDate registrationDate;
    private String grade;
    private boolean isActive;
}