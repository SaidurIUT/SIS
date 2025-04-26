package com.iut.sis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SectionDto {
    private int id;
    private int sectionNumber;
    private CourseDto course;
    private SemesterDto semester;
}