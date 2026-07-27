package com.leavehub.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "PUBLIC_HOLIDAY")
@Data
public class PublicHoliday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOLIDAY_ID")
    private Long id;

    @Column(name = "HOLIDAY_DATE", nullable = false, unique = true)
    private LocalDate holidayDate;

    @Column(name = "DESCRIPTION")
    private String description;
}