package com.iut.sis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admin_info")
@NoArgsConstructor
@Getter
@Setter
public class AdminInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "admin_role")
    private String adminRole;

    @Column(name = "responsibilities")
    private String responsibilities;
}