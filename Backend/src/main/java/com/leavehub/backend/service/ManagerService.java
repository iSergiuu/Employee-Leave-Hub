package com.leavehub.backend.service;

import com.leavehub.backend.dto.ManagerRequestDTO;
import com.leavehub.backend.entity.Department;
import com.leavehub.backend.entity.Employee;
import com.leavehub.backend.entity.LeaveRequest;
import com.leavehub.backend.entity.LeaveWorkflow;
import com.leavehub.backend.repository.DepartmentRepository;
import com.leavehub.backend.repository.EmployeeRepository;
import com.leavehub.backend.repository.LeaveRequestRepository;
import com.leavehub.backend.repository.LeaveWorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveWorkflowRepository leaveWorkflowRepository;

    public List<ManagerRequestDTO> getDepartmentRequests(String managerEmail) {
        Employee manager = employeeRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Managerul nu a fost găsit."));

        Department department = departmentRepository.findByManager(manager)
                .orElseThrow(() -> new RuntimeException("Nu ești asignat ca manager la niciun departament."));

        List<LeaveRequest> requests = leaveRequestRepository.findByEmployeeDepartmentIdOrderByCreatedAtDesc(department.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return requests.stream().map(req -> {
            String formattedLeaveType = req.getLeaveType().getName() + " (" + req.getLeaveType().getCode() + ")";
            String formattedPeriod = req.getStartDate().format(formatter) + " - " + req.getEndDate().format(formatter);
            String createdAt = req.getCreatedAt() != null ? req.getCreatedAt().toLocalDate().format(formatter) : "";

            return new ManagerRequestDTO(
                    req.getId(),
                    req.getEmployee().getName(),
                    formattedLeaveType,
                    formattedPeriod,
                    req.getWorkingDays(),
                    req.getStatus(),
                    createdAt
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public void processRequest(Long requestId, String managerEmail, String newStatus, String comment) {
        Employee manager = employeeRepository.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager invalid."));

        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Cererea nu a fost găsită."));

        if ("REJECTED".equalsIgnoreCase(newStatus) && (comment == null || comment.trim().isEmpty())) {
            throw new RuntimeException("Comentariul este obligatoriu pentru respingerea unei cereri.");
        }

        String oldStatus = request.getStatus();
        request.setStatus(newStatus.toUpperCase());
        leaveRequestRepository.save(request);

        LeaveWorkflow workflow = new LeaveWorkflow();
        workflow.setLeaveRequest(request);
        workflow.setEmployee(manager); // Salvăm cine a luat decizia (managerul)
        workflow.setOldStatus(oldStatus);
        workflow.setCurrentStatus(newStatus.toUpperCase());
        workflow.setChangedAt(LocalDateTime.now());
        workflow.setComments(comment);

        leaveWorkflowRepository.save(workflow);
    }
}