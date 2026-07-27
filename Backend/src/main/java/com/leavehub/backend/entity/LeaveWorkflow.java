package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "LEAVE_WORKFLOW")
@Data
public class LeaveWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKFLOW_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LEAVE_REQUEST_ID", nullable = false)
    private LeaveRequest leaveRequest;

    // Angajatul care a facut schimbarea (ex: managerul care aproba)
    @ManyToOne
    @JoinColumn(name = "EMPL_ID", nullable = false)
    private Employee employee;

    @Column(name = "OLD_STATUS")
    private String oldStatus;

    @Column(name = "CURRENT_STATUS", nullable = false)
    private String currentStatus;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "CHANGED_AT", insertable = false, updatable = false)
    private LocalDateTime changedAt;
}