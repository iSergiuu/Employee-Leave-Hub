package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "DEPARTMENT")
@Data
@SQLDelete(sql = "UPDATE department SET deleted_at = CURRENT_TIMESTAMP WHERE dept_id = ?")
@SQLRestriction("deleted_at IS NULL")
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

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;
}