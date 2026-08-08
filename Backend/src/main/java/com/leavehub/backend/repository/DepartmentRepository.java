package com.leavehub.backend.repository;

import com.leavehub.backend.entity.Department;
import com.leavehub.backend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    Optional<Department> findByManager(Employee manager);

}