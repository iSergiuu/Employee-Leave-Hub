import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ManagerService } from '../../services/manager.service';

@Component({
  selector: 'app-manager-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './manager-dashboard.html',
  styleUrls: ['./manager-dashboard.scss']
})
export class ManagerDashboard implements OnInit {
  
  requests: any[] = [];
  filteredRequests: any[] = [];
  
  filterStatus: string = 'Toate';
  filterEmployee: string = '';
  filterLeaveType: string = 'Toate';
  filterDate: string = '';

  isModalOpen: boolean = false;
  actionType: 'APPROVE' | 'REJECT' = 'APPROVE';
  selectedRequestId!: number;
  actionComment: string = '';
  commentError: boolean = false;

  constructor(private managerService: ManagerService) {}

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests(): void {
    const userEmail = localStorage.getItem('email') || '';
    this.managerService.getDepartmentRequests(userEmail).subscribe({
      next: (data) => {
        this.requests = data;
        this.applyFilters();
      },
      error: (err) => console.error(err)
    });
  }

  applyFilters(): void {
    this.filteredRequests = this.requests.filter(req => {
      const matchStatus = this.filterStatus === 'Toate' || req.status.toUpperCase() === this.filterStatus.toUpperCase();
      const matchEmployee = req.employeeName.toLowerCase().includes(this.filterEmployee.toLowerCase());
      const matchType = this.filterLeaveType === 'Toate' || req.leaveType.includes(this.filterLeaveType);
      
      let matchDate = true;
      if (this.filterDate) {
        const dateObj = new Date(this.filterDate);
        const day = dateObj.getDate().toString().padStart(2, '0');
        const month = (dateObj.getMonth() + 1).toString().padStart(2, '0');
        const year = dateObj.getFullYear();
        const formattedSearchDate = `${day}/${month}/${year}`;
        
        matchDate = req.period.includes(formattedSearchDate);
      }

      return matchStatus && matchEmployee && matchType && matchDate;
    });
  }

  getStatusClass(status: string): string {
    switch (status.toUpperCase()) {
      case 'APPROVED': return 'status-approved';
      case 'PENDING': return 'status-pending';
      case 'REJECTED': return 'status-rejected';
      case 'CANCELLED': return 'status-draft';
      default: return '';
    }
  }

  openActionModal(id: number, type: 'APPROVE' | 'REJECT'): void {
    this.selectedRequestId = id;
    this.actionType = type;
    this.actionComment = '';
    this.commentError = false;
    this.isModalOpen = true;
  }

  closeActionModal(): void {
    this.isModalOpen = false;
  }

  submitAction(): void {
    if (this.actionType === 'REJECT' && !this.actionComment.trim()) {
      this.commentError = true;
      return;
    }

    const userEmail = localStorage.getItem('email') || '';
    const actionObs = this.actionType === 'APPROVE' 
      ? this.managerService.approveRequest(this.selectedRequestId, userEmail, this.actionComment)
      : this.managerService.rejectRequest(this.selectedRequestId, userEmail, this.actionComment);

    actionObs.subscribe({
      next: () => {
        this.closeActionModal();
        this.loadRequests();
      },
      error: (err) => alert(err.error || 'A apărut o eroare.')
    });
  }
}