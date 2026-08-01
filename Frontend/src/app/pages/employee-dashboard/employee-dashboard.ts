import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LeaveTypeService, LeaveType } from '../../services/leave-type.service';

@Component({
  selector: 'app-employee-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './employee-dashboard.html',
  styleUrl: './employee-dashboard.scss'
})
export class EmployeeDashboard implements OnInit {
  
  leaveTypesData: LeaveType[] = [];

  constructor(private leaveTypeService: LeaveTypeService) {}

  ngOnInit(): void {
    this.leaveTypeService.getLeaveTypes().subscribe({
      next: (data) => {
        this.leaveTypesData = data;
      },
      error: (err) => {
        console.error('Eroare la aducerea datelor:', err);
      }
    });
  }
}