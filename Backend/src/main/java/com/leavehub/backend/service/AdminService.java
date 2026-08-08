package com.leavehub.backend.service;

import com.leavehub.backend.dto.AdminDTO;
import com.leavehub.backend.entity.Department;
import com.leavehub.backend.entity.Employee;
import com.leavehub.backend.entity.PublicHoliday;
import com.leavehub.backend.repository.DepartmentRepository;
import com.leavehub.backend.repository.EmployeeRepository;
import com.leavehub.backend.repository.PublicHolidayRepository;
import com.leavehub.backend.repository.LeaveWorkflowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final PublicHolidayRepository publicHolidayRepository;
    private final LeaveWorkflowRepository leaveWorkflowRepository;

    public List<AdminDTO.EmployeeInfo> getAllEmployees() {
        return employeeRepository.findAll().stream().map(emp -> {
            AdminDTO.EmployeeInfo dto = new AdminDTO.EmployeeInfo();
            dto.setId(emp.getId());
            dto.setName(emp.getName());
            dto.setEmail(emp.getEmail());
            dto.setRole(emp.getRole());

            if (emp.getDepartment() != null) {
                dto.setDeptId(emp.getDepartment().getId());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void updateEmployeeRoleAndDept(Long employeeId, String role, Long deptId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Angajatul nu a fost gasit"));

        Department dept = null;
        if (deptId != null) {
            dept = departmentRepository.findById(deptId)
                    .orElseThrow(() -> new RuntimeException("Departamentul nu a fost gasit"));
        }

        // Salvăm modificările pe angajat
        employee.setRole(role);
        employee.setDepartment(dept);
        employeeRepository.save(employee);

        // Gestionăm legătura de manager în tabela DEPARTMENT
        if (dept != null) {
            if ("Dept_resp".equalsIgnoreCase(role) || "Manager".equalsIgnoreCase(role)) {
                // Dacă departamentul are deja un manager și nu este angajatul pe care îl edităm acum
                if (dept.getManager() != null && !dept.getManager().getId().equals(employee.getId())) {
                    throw new RuntimeException("Eroare: Departamentul " + dept.getDepartmentName() +
                            " are deja un manager alocat (" + dept.getManager().getName() + ").");
                }
                dept.setManager(employee);
                departmentRepository.save(dept);
            } else {
                // Dacă i se ia rolul de manager, îl scoatem de la conducerea departamentului
                if (dept.getManager() != null && dept.getManager().getId().equals(employee.getId())) {
                    dept.setManager(null);
                    departmentRepository.save(dept);
                }
            }
        } else {
            // Dacă angajatul este mutat pe "Fără departament", verificăm dacă era manager undeva și îl scoatem
            departmentRepository.findByManager(employee).ifPresent(d -> {
                d.setManager(null);
                departmentRepository.save(d);
            });
        }
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<AdminDTO.DepartmentInfo> getAllDepartments() {
        return departmentRepository.findAll().stream().map(dept -> {
            AdminDTO.DepartmentInfo dto = new AdminDTO.DepartmentInfo();
            dto.setId(dept.getId());
            dto.setName(dept.getDepartmentName());
            dto.setMaxAbsentEmployees(dept.getMaxAbsentEmployees());

            // Setăm numele managerului
            if (dept.getManager() != null) {
                dto.setManagerName(dept.getManager().getName());
            } else {
                dto.setManagerName("Fără Manager");
            }

            // Extragem lista cu numele tuturor angajaților din acest departament
            List<String> employeeNames = employeeRepository.findByDepartmentId(dept.getId())
                    .stream()
                    .map(Employee::getName)
                    .collect(Collectors.toList());
            dto.setEmployeeNames(employeeNames);

            return dto;
        }).collect(Collectors.toList());
    }

    public void createDepartment(AdminDTO.DepartmentInfo dto) {
        Department dept = new Department();
        dept.setDepartmentName(dto.getName());
        dept.setMaxAbsentEmployees(dto.getMaxAbsentEmployees());
        departmentRepository.save(dept);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    public List<AdminDTO.HolidayInfo> getAllHolidays() {
        return publicHolidayRepository.findAll().stream().map(hol -> {
            AdminDTO.HolidayInfo dto = new AdminDTO.HolidayInfo();
            dto.setId(hol.getId());
            dto.setHolidayDate(hol.getHolidayDate());
            dto.setDescription(hol.getDescription());
            return dto;
        }).collect(Collectors.toList());
    }

    public void createHoliday(AdminDTO.HolidayInfo dto) {
        PublicHoliday holiday = new PublicHoliday();
        holiday.setHolidayDate(dto.getHolidayDate());
        holiday.setDescription(dto.getDescription());
        publicHolidayRepository.save(holiday);
    }

    public void deleteHoliday(Long id) {
        publicHolidayRepository.deleteById(id);
    }

    public List<AdminDTO.TimelineEventInfo> getWorkflowTimeline() {
        return leaveWorkflowRepository.findAll().stream()
                .sorted((a, b) -> b.getChangedAt().compareTo(a.getChangedAt()))
                .limit(20)
                .map(wf -> {
                    AdminDTO.TimelineEventInfo dto = new AdminDTO.TimelineEventInfo();
                    dto.setId(wf.getId());
                    dto.setRequestId(wf.getLeaveRequest().getId());
                    dto.setEmployeeName(wf.getEmployee().getName());
                    dto.setCurrentStatus(wf.getCurrentStatus());
                    dto.setChangedAt(wf.getChangedAt());
                    dto.setComments(wf.getComments());
                    return dto;
                }).collect(Collectors.toList());
    }

    public byte[] generateAdminReport(String reportType) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        com.lowagie.text.Document document = new com.lowagie.text.Document();

        try {
            com.lowagie.text.pdf.PdfWriter.getInstance(document, out);
            document.open();

            document.add(new com.lowagie.text.Paragraph("RAPORT ADMINISTRATIV LEAVE HUB",
                    com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD, 18)));
            document.add(new com.lowagie.text.Paragraph("\nTip Raport: " + reportType.toUpperCase()));
            document.add(new com.lowagie.text.Paragraph("Data generării: " + java.time.LocalDate.now()));
            document.add(new com.lowagie.text.Paragraph("\nAcest document este generat automat din sistem."));

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Eroare la generarea PDF-ului", e);
        }

        return out.toByteArray();
    }
}