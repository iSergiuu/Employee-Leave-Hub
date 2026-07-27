package com.leavehub.backend.repository;

import com.leavehub.backend.entity.PublicHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long> {
}