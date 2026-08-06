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
    switch (status.toUpperCase()) {
      case 'APPROVED': 
      case 'APROBAT': 
        return 'status-approved';
      case 'PENDING': 
      case 'ÎN AȘTEPTARE': 
        return 'status-pending';
      case 'REJECTED': 
      case 'CANCELLED': 
      case 'RESPINS': 
        return 'status-rejected';
      case 'DRAFT': 
      case 'CIORNĂ': 
        return 'status-draft';
      default: 
        return '';
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

  cancelRequest(id: number): void {
    if (confirm('Ești sigur că vrei să anulezi această cerere?')) {
      this.dashboardService.cancelLeaveRequest(id).subscribe({
        next: () => {
          const userEmail = localStorage.getItem('email') || '';
          this.fetchDashboardData(userEmail);
        },
        error: (err) => alert('Eroare la anulare: ' + (err.error || 'Cererea nu mai poate fi anulată.'))
      });
    }
  }

  downloadPdf(id: number): void {
    this.dashboardService.downloadRequestPdf(id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `cerere_concediu_${id}.pdf`;
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      },
      error: () => alert('Eroare la descărcarea documentului.')
    });
  }
}