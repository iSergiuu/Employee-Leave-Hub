import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getEmployees(): Observable<any> {
    return this.http.get(`${this.apiUrl}/employees`, { headers: this.getHeaders() });
  }

  updateEmployee(id: number, data: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/employees/${id}`, data, { headers: this.getHeaders() });
  }

  deleteEmployee(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/employees/${id}`, { headers: this.getHeaders(), responseType: 'text' });
  }

  getDepartments(): Observable<any> {
    return this.http.get(`${this.apiUrl}/departments`, { headers: this.getHeaders() });
  }

  createDepartment(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/departments`, data, { headers: this.getHeaders() });
  }

  deleteDepartment(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/departments/${id}`, { headers: this.getHeaders(), responseType: 'text' });
  }

  getHolidays(): Observable<any> {
    return this.http.get(`${this.apiUrl}/holidays`, { headers: this.getHeaders() });
  }

  createHoliday(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/holidays`, data, { headers: this.getHeaders() });
  }

  deleteHoliday(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/holidays/${id}`, { headers: this.getHeaders(), responseType: 'text' });
  }
  getWorkflowTimeline(): Observable<any> {
    return this.http.get(`${this.apiUrl}/timeline`, { headers: this.getHeaders() });
  }

  downloadReport(reportType: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/reports/${reportType}`, { 
      headers: this.getHeaders(),
      responseType: 'blob' 
    });
  }
}