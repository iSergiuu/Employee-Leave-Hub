import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { EmployeeDashboard } from './pages/employee-dashboard/employee-dashboard';
import { ManagerDashboard } from './pages/manager-dashboard/manager-dashboard';
import { AdminDashboard } from './pages/admin-dashboard/admin-dashboard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
  
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  
  { path: 'employee', component: EmployeeDashboard },
  { path: 'manager', component: ManagerDashboard },
  { path: 'admin', component: AdminDashboard },
  
  { path: '**', redirectTo: 'login' }
];