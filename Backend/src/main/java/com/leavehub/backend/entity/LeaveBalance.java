package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LEAVE_BALANCE")
@Data
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BALANCE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPL_ID", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "LEAVE_TYPE_ID", nullable = false)
    private LeaveType leaveType;

    @Column(name = "YEAR", nullable = false)
    private Integer year;

    @Column(name = "TOTAL_ALLOCATED", nullable = false)
    private Integer totalAllocated;

    @Column(name = "DAYS_USED", nullable = false)
    private Integer daysUsed;
}