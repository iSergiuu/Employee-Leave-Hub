package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "LEAVE_TYPE")
@Data
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LEAVE_TYPE_ID")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "REQUIRES_ATTACHMENT", nullable = false)
    private Boolean requiresAttachment;

    @Column(name = "PAID", nullable = false)
    private Boolean paid;
}
