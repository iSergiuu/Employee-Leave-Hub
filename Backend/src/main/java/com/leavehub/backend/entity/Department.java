package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "DEPARTMENT")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEPT_ID")
    private Long id;

    @Column(name = "DEPARTMENT_NAME", nullable = false)
    private String departmentName;

    @Column(name = "MAX_ABSENT_EMPLOYEES", nullable = false)
    private Integer maxAbsentEmployees;

    @OneToOne
    @JoinColumn(name = "MANAGER_ID")
    private Employee manager;
}