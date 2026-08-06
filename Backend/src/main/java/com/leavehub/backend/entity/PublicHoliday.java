package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "PUBLIC_HOLIDAY")
@Data
@SQLDelete(sql = "UPDATE public_holiday SET deleted_at = CURRENT_TIMESTAMP WHERE holiday_id = ?")
@SQLRestriction("deleted_at IS NULL")
public class PublicHoliday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOLIDAY_ID")
    private Long id;

    @Column(name = "HOLIDAY_DATE", nullable = false, unique = true)
    private LocalDate holidayDate;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "DELETED_AT")
    private LocalDateTime deletedAt;
}