import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, FormsModule],
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.scss']
})
export class AdminDashboard implements OnInit {
  
  activeTab: string = 'employees';

  isDeptModalOpen: boolean = false;
  isHolidayModalOpen: boolean = false;

  deptForm!: FormGroup;
  holidayForm!: FormGroup;

  departments: any[] = [];
  employees: any[] = [];
  holidays: any[] = [];

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.deptForm = new FormGroup({
      departmentName: new FormControl('', Validators.required),
      maxAbsentEmployees: new FormControl('', [Validators.required, Validators.min(1)])
    });

    this.holidayForm = new FormGroup({
      holidayDate: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required)
    });

    this.loadDepartments();
    this.loadEmployees();
    this.loadHolidays();
  }

  loadDepartments(): void {
    this.adminService.getDepartments().subscribe({
      next: (data) => this.departments = data,
      error: (err) => console.error(err)
    });
  }

  loadEmployees(): void {
    this.adminService.getEmployees().subscribe({
      next: (data) => this.employees = data,
      error: (err) => console.error(err)
    });
  }

  loadHolidays(): void {
    this.adminService.getHolidays().subscribe({
      next: (data) => this.holidays = data,
      error: (err) => console.error(err)
    });
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  saveEmployeeChanges(employee: any): void {
    const payload = {
      role: employee.role,
      deptId: employee.deptId
    };
    
    this.adminService.updateEmployee(employee.id, payload).subscribe({
      next: () => alert('Modificările au fost salvate cu succes!'),
      error: (err) => console.error(err)
    });
  }

  deleteEmployee(id: number): void {
    if (confirm('Atenție: Ești sigur că vrei să ștergi acest angajat?')) {
      this.adminService.deleteEmployee(id).subscribe({
        next: () => this.loadEmployees(),
        error: (err) => alert(err.error || 'A apărut o eroare la ștergere.')
      });
    }
  }

  deleteDepartment(id: number): void {
    if (confirm('Atenție: Ești sigur că vrei să ștergi acest departament?')) {
      this.adminService.deleteDepartment(id).subscribe({
        next: () => this.loadDepartments(),
        error: (err) => alert(err.error || 'A apărut o eroare la ștergere.')
      });
    }
  }

  deleteHoliday(id: number): void {
    if (confirm('Ești sigur că vrei să ștergi această sărbătoare?')) {
      this.adminService.deleteHoliday(id).subscribe({
        next: () => this.loadHolidays(),
        error: (err) => alert(err.error || 'A apărut o eroare la ștergere.')
      });
    }
  }

  openDeptModal(): void {
    this.isDeptModalOpen = true;
    this.deptForm.reset();
  }

  closeDeptModal(): void {
    this.isDeptModalOpen = false;
  }

  onSubmitDept(): void {
    if (this.deptForm.valid) {
      const payload = {
        name: this.deptForm.value.departmentName,
        maxAbsentEmployees: this.deptForm.value.maxAbsentEmployees
      };

      this.adminService.createDepartment(payload).subscribe({
        next: () => {
          this.closeDeptModal();
          this.loadDepartments();
        },
        error: (err) => console.error(err)
      });
    } else {
      this.deptForm.markAllAsTouched();
    }
  }

  openHolidayModal(): void {
    this.isHolidayModalOpen = true;
    this.holidayForm.reset();
  }

  closeHolidayModal(): void {
    this.isHolidayModalOpen = false;
  }

  onSubmitHoliday(): void {
    if (this.holidayForm.valid) {
      this.adminService.createHoliday(this.holidayForm.value).subscribe({
        next: () => {
          this.closeHolidayModal();
          this.loadHolidays();
        },
        error: (err) => console.error(err)
      });
    } else {
      this.holidayForm.markAllAsTouched();
    }
  }
}