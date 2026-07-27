package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "LEAVE_REQUEST")
@Data
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LEAVE_REQUEST_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EMPL_ID", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "LEAVE_TYPE_ID", nullable = false)
    private LeaveType leaveType;

    @Column(name = "START_DATE", nullable = false)
    private LocalDate startDate;

    @Column(name = "END_DATE", nullable = false)
    private LocalDate endDate;

    @Column(name = "WORKING_DAYS", nullable = false)
    private Integer workingDays;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "CREATED_AT", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}