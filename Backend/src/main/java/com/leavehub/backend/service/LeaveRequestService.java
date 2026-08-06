package com.leavehub.backend.service;

import com.leavehub.backend.entity.Employee;
import com.leavehub.backend.entity.LeaveRequest;
import com.leavehub.backend.entity.LeaveType;
import com.leavehub.backend.entity.PublicHoliday;
import com.leavehub.backend.repository.LeaveRequestRepository;
import com.leavehub.backend.repository.PublicHolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final PublicHolidayRepository publicHolidayRepository;
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
    public LeaveRequest submitLeaveRequest(Long employeeId, LeaveType leaveType, LocalDate startDate, LocalDate endDate) {

        Integer requiredDays = calculateWorkingDays(startDate, endDate);
        if (requiredDays == 0) {
            throw new RuntimeException("Intervalul selectat nu conține zile lucrătoare.");
        }

        boolean hasDays = leaveBalanceService.hasSufficientDays(employeeId, leaveType.getId(), startDate.getYear(), requiredDays);
        if (!hasDays) {
            throw new RuntimeException("Nu ai suficiente zile disponibile pentru acest concediu.");
        }

        Employee employee = employeeService.getEmployeeById(employeeId);

        LeaveRequest request = new LeaveRequest();
        request.setEmployee(employee);
        request.setLeaveType(leaveType);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setWorkingDays(requiredDays);
        request.setStatus("PENDING");

        return leaveRequestRepository.save(request);
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
}