package com.iut.sis.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class AdminInfoDto {
    private int id;
    private UserDto user;
    private String adminRole;
    private String responsibilities;
}
