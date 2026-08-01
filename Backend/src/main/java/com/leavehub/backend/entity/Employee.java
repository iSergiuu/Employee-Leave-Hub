package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "EMPLOYEE")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPL_ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "ROLE", nullable = false)
    private String role;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "DEPT_ID")
    private Department department;
}