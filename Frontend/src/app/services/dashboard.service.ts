import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  
  private apiUrl = 'http://localhost:8080/api/dashboard';
  private requestApiUrl = 'http://localhost:8080/api/leave-requests';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || '';
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getEmployeeDashboard(email: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/employee/${email}`, { headers: this.getHeaders() });
  }

  createLeaveRequest(requestData: any): Observable<any> {
    return this.http.post(this.requestApiUrl, requestData, { headers: this.getHeaders() });
  }

  cancelLeaveRequest(id: number): Observable<any> {
    return this.http.put(`${this.requestApiUrl}/${id}/cancel`, {}, { headers: this.getHeaders() });
  }

  downloadRequestPdf(id: number): Observable<Blob> {
    return this.http.get(`${this.requestApiUrl}/${id}/pdf`, { 
      headers: this.getHeaders(),
      responseType: 'blob' 
    });
  }
}