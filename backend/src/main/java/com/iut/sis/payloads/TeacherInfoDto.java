package com.iut.sis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class TeacherInfoDto {
    private int id;
    private UserDto user;
    private DepartmentDto department;
    private String designation;
    private String joiningDate;
}