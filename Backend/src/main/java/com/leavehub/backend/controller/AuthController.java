package com.leavehub.backend.controller;

import com.leavehub.backend.dto.LoginRequest;
import com.leavehub.backend.dto.RegisterRequest;
import com.leavehub.backend.entity.Employee;
import com.leavehub.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Eroare: Adresa de email este deja folosită!");
        }

        Employee newEmployee = new Employee();
        newEmployee.setName(request.getFullname());
        newEmployee.setEmail(request.getEmail());
        newEmployee.setRole("User");

        newEmployee.setPassword(passwordEncoder.encode(request.getPassword()));

        employeeRepository.save(newEmployee);

        return ResponseEntity.ok("Cont creat cu succes!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {

        Optional<Employee> employeeOptional = employeeRepository.findByEmail(request.getEmail());

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();

            if (passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
                return ResponseEntity.ok("Logare reușită! (Mai târziu vom genera un token JWT aici)");
            }
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Eroare: Email sau parolă incorecte!");
    }
}