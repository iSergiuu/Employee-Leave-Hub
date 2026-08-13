import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-calendar',
  standalone: true,
  imports: [CommonModule, FullCalendarModule],
  templateUrl: './calendar.html',
  styleUrls: ['./calendar.scss']
})
export class CalendarComponent implements OnInit {
  userRole: string = '';
  userEmail: string = '';
  currentView: string = 'personal';
  isManagerOrAdmin: boolean = false; 
  
  currentDisplayedDate: Date = new Date();
  holidayEvents: any[] = [];

  calendarOptions: any = {
    plugins: [dayGridPlugin, interactionPlugin],
    initialView: 'dayGridMonth',
    locale: 'ro',
    firstDay: 1,
    height: 'auto',
    contentHeight: 'auto',
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: ''
    },
    buttonText: { 
      today: 'Azi' 
    },
    events: [],
    datesSet: this.handleDatesSet.bind(this) 
  };

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.userRole = (localStorage.getItem('userRole') || 'User').trim();
    this.userEmail = localStorage.getItem('email') || '';
    this.isManagerOrAdmin = ['Manager', 'MANAGER', 'Dept_resp', 'ROLE_MANAGER', 'Admin'].includes(this.userRole);
    
    this.loadHolidays();
    this.loadCalendarData('personal');
  }

  loadHolidays(): void {
    this.dashboardService.getHolidays().subscribe({
      next: (holidays: any[]) => {
        this.holidayEvents = holidays.map(h => {
          const rawDate = h.holidayDate;
          const parseDate = (d: any) => Array.isArray(d) ? new Date(d[0], d[1] - 1, d[2]) : new Date(d);
          const dateObj = parseDate(rawDate);

          return {
            title: `🌟 ${h.description}`,
            start: dateObj.toISOString().split('T')[0],
            color: '#8b5cf6',
            display: 'block',
            allDay: true
          };
        });
        this.loadCalendarData(this.currentView);
      },
      error: (err) => console.error(err)
    });
  }

  handleDatesSet(arg: any): void {
    this.currentDisplayedDate = arg.view.calendar.getDate();
  }

  onViewChange(view: string): void {
    this.currentView = view;
    this.loadCalendarData(view);
  }

  loadCalendarData(view: string): void {
    if (view === 'personal') {
      this.dashboardService.getEmployeeHistory(this.userEmail).subscribe({
        next: (requests: any[]) => this.mapRequestsToEvents(requests, true),
        error: (err: any) => console.error(err)
      });
    } else if (view === 'department' && this.isManagerOrAdmin) {
      this.dashboardService.getDepartmentRequests(this.userEmail).subscribe({
        next: (requests: any[]) => this.mapRequestsToEvents(requests, false),
        error: (err: any) => console.error(err)
      });
    }
  }

  downloadDepartmentReport(): void {
    const month = this.currentDisplayedDate.getMonth() + 1; 
    const year = this.currentDisplayedDate.getFullYear();

    this.dashboardService.downloadDepartmentReportPdf(this.userEmail, month, year).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Raport_Departament_${month}_${year}.pdf`;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      error: (err) => alert(err)
    });
  }

  mapRequestsToEvents(requests: any[], isPersonal: boolean): void {
    const leaveEvents = requests
      .map(req => {
        const rawStart = req.startDate || req.start_date;
        const rawEnd = req.endDate || req.end_date;

        if (!rawStart || !rawEnd) return null;

        let eventColor = '#3b82f6';
        if (req.status === 'APPROVED') eventColor = '#10b981';
        else if (req.status === 'PENDING') eventColor = '#f59e0b';
        else if (req.status === 'REJECTED' || req.status === 'CANCELLED') eventColor = '#ef4444';

        const parseDate = (d: any) => Array.isArray(d) ? new Date(d[0], d[1] - 1, d[2]) : new Date(d);

        const startDate = parseDate(rawStart);
        const endDate = parseDate(rawEnd);
        endDate.setDate(endDate.getDate() + 1);

        return {
          title: isPersonal 
            ? `${req.leaveType?.code || 'Concediu'} - ${req.status}` 
            : `${req.leaveType?.code || 'CO'} - ${req.employee?.name || 'Necunoscut'}`,
          start: startDate.toISOString().split('T')[0],
          end: endDate.toISOString().split('T')[0],
          color: eventColor
        };
      })
      .filter(event => event !== null);

    this.calendarOptions = { 
      ...this.calendarOptions, 
      events: [...this.holidayEvents, ...leaveEvents] 
    };
  }
}