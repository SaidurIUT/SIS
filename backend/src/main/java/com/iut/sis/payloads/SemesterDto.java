package com.iut.sis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
public class SemesterDto {
    private int id;
    private String name;
    private int semesterNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCurrent;
    private EducationalSessionDto session;
}