import { Component, OnInit } from '@angular/core';
import { CommonModule, formatDate } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule, FormGroup, FormControl, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './employee-dashboard.html',
  styleUrls: ['./employee-dashboard.scss']
})
export class EmployeeDashboard implements OnInit {
  
  userName: string = '';
  currentDate: string = '';
  leaveTypesData: any[] = [];

  summaryStats = {
    availableDays: 0,
    usedDays: 0,
    pendingRequests: 0,
    approvedRequests: 0
  };

  recentRequests: any[] = [];
  leaveBalances: any[] = [];

  isModalOpen: boolean = false;
  requestForm!: FormGroup;

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.userName = localStorage.getItem('userName') || '';
    this.currentDate = formatDate(new Date(), 'd MMM yyyy', 'en-US');
    
    const userEmail = localStorage.getItem('email') || '';

    if (userEmail) {
      this.fetchDashboardData(userEmail);
    }

    this.requestForm = new FormGroup({
      leaveTypeId: new FormControl('', Validators.required),
      startDate: new FormControl('', Validators.required),
      endDate: new FormControl('', Validators.required),
      reason: new FormControl('', Validators.required)
    }, { validators: this.dateRangeValidator });
  }

  fetchDashboardData(email: string): void {
    this.dashboardService.getEmployeeDashboard(email).subscribe({
      next: (data) => {
        this.summaryStats = data.summaryStats;
        this.recentRequests = data.recentRequests;
        this.leaveBalances = data.leaveBalances;
      },
      error: (err) => {
        console.error(err);
      }
    });
  }

  dateRangeValidator(group: AbstractControl): ValidationErrors | null {
    const start = group.get('startDate')?.value;
    const end = group.get('endDate')?.value;

    if (start && end) {
      const startDate = new Date(start);
      const endDate = new Date(end);

      if (startDate > endDate) {
        return { invalidDateRange: true };
      }
    }
    return null;
  }

  getStatusClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'aprobat': return 'status-approved';
      case 'în așteptare': return 'status-pending';
      case 'respins': return 'status-rejected';
      case 'ciornă': return 'status-draft';
      default: return '';
    }
  }

  openRequestModal(): void {
    this.isModalOpen = true;
    this.requestForm.reset();
    this.requestForm.patchValue({ leaveTypeId: '' });
  }

  closeRequestModal(): void {
    this.isModalOpen = false;
  }

  onSubmitRequest(): void {
    if (this.requestForm.valid) {
      const userEmail = localStorage.getItem('email') || '';
      
      const newRequest = {
        email: userEmail,
        leaveTypeId: this.requestForm.value.leaveTypeId,
        startDate: this.requestForm.value.startDate,
        endDate: this.requestForm.value.endDate,
        reason: this.requestForm.value.reason
      };

      this.dashboardService.createLeaveRequest(newRequest).subscribe({
        next: () => {
          this.closeRequestModal();
          this.fetchDashboardData(userEmail);
        },
        error: (err) => {
          console.error(err);
        }
      });
    } else {
      this.requestForm.markAllAsTouched();
    }
  }
}