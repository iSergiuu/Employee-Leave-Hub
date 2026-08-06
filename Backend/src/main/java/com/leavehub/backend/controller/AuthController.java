package com.leavehub.backend.controller;

import com.leavehub.backend.dto.AuthResponse;
import com.leavehub.backend.dto.LoginRequest;
import com.leavehub.backend.dto.RegisterRequest;
import com.leavehub.backend.entity.Employee;
import com.leavehub.backend.repository.EmployeeRepository;
import com.leavehub.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

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

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Angajatul nu a fost găsit"));

        String jwt = jwtUtil.generateToken(employee.getEmail());

        return ResponseEntity.ok(new AuthResponse(jwt, employee.getName(), employee.getRole()));
    }
}