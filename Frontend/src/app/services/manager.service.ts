import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ManagerService {
  
  private apiUrl = 'http://localhost:8080/api/manager';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getDepartmentRequests(email: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/requests?email=${email}`, { headers: this.getHeaders() });
  }

  approveRequest(id: number, email: string, comment: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/requests/${id}/approve`, { email, comment }, { headers: this.getHeaders() });
  }

  rejectRequest(id: number, email: string, comment: string): Observable<any> {
    return this.http.put(`${this.apiUrl}/requests/${id}/reject`, { email, comment }, { headers: this.getHeaders() });
  }
}