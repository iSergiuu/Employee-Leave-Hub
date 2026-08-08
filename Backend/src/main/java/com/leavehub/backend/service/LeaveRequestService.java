package com.leavehub.backend.service;

import com.leavehub.backend.dto.NewLeaveRequestDTO;
import com.leavehub.backend.entity.*;
import com.leavehub.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final PublicHolidayRepository publicHolidayRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveWorkflowRepository leaveWorkflowRepository;
    private final EmployeeService employeeService;
    private final LeaveBalanceService leaveBalanceService;

    public Integer calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Data de început nu poate fi după data de sfârșit.");
        }

        List<LocalDate> holidays = publicHolidayRepository.findByHolidayDateBetween(startDate, endDate)
                .stream()
                .map(PublicHoliday::getHolidayDate)
                .toList();

        int workingDays = 0;
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            boolean isWeekend = currentDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    currentDate.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isHoliday = holidays.contains(currentDate);

            if (!isWeekend && !isHoliday) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }

        return workingDays;
    }

    @Transactional
    public LeaveRequest submitLeaveRequest(NewLeaveRequestDTO dto) {
        Employee employee = employeeRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Angajatul nu a fost găsit."));

        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Tipul de concediu nu a fost găsit."));

        Integer requiredDays = calculateWorkingDays(dto.getStartDate(), dto.getEndDate());
        if (requiredDays == 0) {
            throw new RuntimeException("Intervalul selectat nu conține zile lucrătoare.");
        }

        boolean hasDays = leaveBalanceService.hasSufficientDays(employee.getId(), leaveType.getId(), dto.getStartDate().getYear(), requiredDays);
        if (!hasDays) {
            throw new RuntimeException("Nu ai suficiente zile disponibile pentru acest concediu.");
        }

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(leaveType);
        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setWorkingDays(requiredDays);
        request.setStatus("PENDING");
        request.setCreatedAt(LocalDateTime.now());

        LeaveRequest savedRequest = leaveRequestRepository.save(request);

        LeaveWorkflow workflow = new LeaveWorkflow();
        workflow.setLeaveRequest(savedRequest);
        workflow.setEmployee(employee);
        workflow.setOldStatus(null);
        workflow.setCurrentStatus("PENDING");
        workflow.setChangedAt(LocalDateTime.now());
        workflow.setComments(dto.getReason() != null ? dto.getReason() : "Cerere inițială trimisă din portal.");
        leaveWorkflowRepository.save(workflow);

        return savedRequest;
    }

    public LeaveRequest findById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cererea de concediu cu ID-ul " + id + " nu a fost găsită."));
    }

    @Transactional
    public LeaveRequest cancelRequest(Long id) {
        LeaveRequest request = findById(id);

        if ("APPROVED".equalsIgnoreCase(request.getStatus()) || "REJECTED".equalsIgnoreCase(request.getStatus())) {
            throw new RuntimeException("Cererea nu mai poate fi anulată deoarece a fost deja procesată (" + request.getStatus() + ").");
        }

        request.setStatus("CANCELLED");
        return leaveRequestRepository.save(request);
    }

    public boolean hasOverlapWarning(Long requestId) {
        LeaveRequest request = findById(requestId);
        if (request.getEmployee().getDepartment() == null) return false;

        Department dept = request.getEmployee().getDepartment();
        int limit = dept.getMaxAbsentEmployees();

        List<LeaveRequest> approvedRequests = leaveRequestRepository
                .findApprovedByDepartmentAndDateRange(dept.getId(), request.getStartDate(), request.getEndDate());

        if (approvedRequests.isEmpty()) return false;

        LocalDate currentDate = request.getStartDate();
        while (!currentDate.isAfter(request.getEndDate())) {
            LocalDate checkDate = currentDate;

            if (checkDate.getDayOfWeek() == DayOfWeek.SATURDAY || checkDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                currentDate = currentDate.plusDays(1);
                continue;
            }

            long absentCount = approvedRequests.stream()
                    .filter(r -> !checkDate.isBefore(r.getStartDate()) && !checkDate.isAfter(r.getEndDate()))
                    .count();

            if (absentCount >= limit) {
                return true;
            }

            currentDate = currentDate.plusDays(1);
        }

        return false;
    }
}