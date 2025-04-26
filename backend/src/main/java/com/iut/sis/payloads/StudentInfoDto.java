package com.iut.sis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class StudentInfoDto {
    private String studentId;
    private UserDto user;
    private DepartmentDto department;
    private CourseDto course;
    private String batch;
    private int section;
    private int roll;
    private int currentSemester;
}
