package com.iut.sis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class SubjectDto {
    private int id;
    private String name;
    private String code;
    private double creditHours;
    private int semester;
    private DepartmentDto department;
}