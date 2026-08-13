import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'; 

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.scss']
})
export class SidebarComponent implements OnInit {
  
  userName: string = '';
  userRole: string = '';
  displayRole: string = 'Angajat';
  
  isManager: boolean = false;
  isAdmin: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.userName = this.authService.getUserName();
    this.userRole = (localStorage.getItem('userRole') || 'Angajat').trim();

    this.isManager = ['Manager', 'MANAGER', 'Dept_resp', 'ROLE_MANAGER', 'Admin', 'ADMIN'].includes(this.userRole);
    this.isAdmin = ['Admin', 'ADMIN', 'ROLE_ADMIN'].includes(this.userRole);

    if (this.isAdmin) {
      this.displayRole = 'Administrator';
    } else if (this.isManager) {
      this.displayRole = 'Manager';
    } else {
      this.displayRole = 'Angajat';
    }
  }

  onLogout() {
    this.authService.logout(); 
    localStorage.clear(); 
    this.router.navigate(['/login']);
  }
}