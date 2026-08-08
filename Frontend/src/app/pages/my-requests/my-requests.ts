import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-my-requests',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './my-requests.html',
  styleUrls: ['./my-requests.scss']
})
export class MyRequestsComponent implements OnInit {
  requests: any[] = [];
  filteredRequests: any[] = [];
  availableYears: string[] = [];

  statusFilter: string = 'ALL';
  searchQuery: string = '';
  selectedYear: string = 'ALL';
  sortOption: string = 'DATE_DESC';

  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests(): void {
    const userEmail = localStorage.getItem('userEmail') || '';
    this.isLoading = true;

    this.dashboardService.getEmployeeHistory(userEmail).subscribe({
      next: (data: any[]) => {
        this.requests = data;
        this.extractYears();
        this.applyFilters();
        this.isLoading = false;
      },
      error: (err: any) => {
        this.errorMessage = err.error || 'Eroare la preluarea datelor.';
        this.isLoading = false;
        console.error(err);
      }
    });
  }

  extractYears(): void {
    const years = this.requests
      .map(req => new Date(req.startDate).getFullYear().toString())
      .filter(year => year !== 'NaN');
    this.availableYears = Array.from(new Set(years)).sort((a, b) => b.localeCompare(a));
  }

  setFilterStatus(status: string): void {
    this.statusFilter = status;
    this.applyFilters();
  }

  applyFilters(): void {
    let result = [...this.requests];

    if (this.statusFilter !== 'ALL') {
      result = result.filter(req => req.status === this.statusFilter);
    }

    if (this.selectedYear !== 'ALL') {
      result = result.filter(req => new Date(req.startDate).getFullYear().toString() === this.selectedYear);
    }

    if (this.searchQuery.trim() !== '') {
      const query = this.searchQuery.toLowerCase();
      result = result.filter(req => (req.leaveType?.name || '').toLowerCase().includes(query));
    }

    switch (this.sortOption) {
      case 'DATE_DESC':
        result.sort((a, b) => new Date(b.startDate).getTime() - new Date(a.startDate).getTime());
        break;
      case 'DATE_ASC':
        result.sort((a, b) => new Date(a.startDate).getTime() - new Date(b.startDate).getTime());
        break;
      case 'ALPHA_ASC':
        result.sort((a, b) => (a.leaveType?.name || '').localeCompare(b.leaveType?.name || ''));
        break;
      case 'ALPHA_DESC':
        result.sort((a, b) => (b.leaveType?.name || '').localeCompare(a.leaveType?.name || ''));
        break;
    }

    this.filteredRequests = result;
  }

  cancelRequest(id: number): void {
    if(confirm('Confirmare anulare cerere concediu. Continuați?')) {
      this.dashboardService.cancelLeaveRequest(id).subscribe({
        next: () => {
          this.loadRequests();
        },
        error: (err: any) => {
          alert('Eroare la anularea cererii: ' + (err.error || err.message));
        }
      });
    }
  }

  downloadPdf(id: number): void {
    this.dashboardService.downloadRequestPdf(id).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Cerere_Concediu_${id}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err: any) => console.error('Eroare generare PDF', err)
    });
  }
}