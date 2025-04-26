package com.iut.sis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CourseDto {
    private int id;
    private String name;
    private int code;
    private DepartmentDto department;
}