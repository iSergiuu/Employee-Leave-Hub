import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { EmployeeDashboard } from './pages/employee-dashboard/employee-dashboard';
import { ManagerDashboard } from './pages/manager-dashboard/manager-dashboard';
import { AdminDashboard } from './pages/admin-dashboard/admin-dashboard';
import { MyRequestsComponent } from './pages/my-requests/my-requests'; 

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' }, 
  
  { path: 'login', component: Login },
  { path: 'register', component: RegisterComponent },
  
  { path: 'employee', component: EmployeeDashboard },
  { path: 'manager', component: ManagerDashboard },
  { path: 'admin', component: AdminDashboard },
  
  { path: 'my-requests', component: MyRequestsComponent },
  
  { path: '**', redirectTo: 'login' }
];