package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ATTACHMENT")
@Data
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ATTACHMENT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "LEAVE_REQUEST_ID", nullable = false)
    private LeaveRequest leaveRequest;

    @Column(name = "FILE_NAME", nullable = false)
    private String fileName;

    @Column(name = "FILE_PATH", nullable = false)
    private String filePath;

    @Column(name = "UPLOADED_AT", insertable = false, updatable = false)
    private LocalDateTime uploadedAt;
}