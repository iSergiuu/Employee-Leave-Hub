package com.leavehub.backend.service;

import com.leavehub.backend.entity.Employee;
import com.leavehub.backend.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Angajatul cu ID-ul " + id + " nu a fost găsit în sistem."));
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}