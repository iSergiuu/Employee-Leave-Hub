import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface LeaveType {
  id?: number;
  name: string;
  code: string;
  requiresAttachment: boolean;
  paid: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class LeaveTypeService {

  private apiUrl = 'http://localhost:8080/api/leave-types';

  constructor(private http: HttpClient) { }

  getLeaveTypes(): Observable<LeaveType[]> {
    return this.http.get<LeaveType[]>(this.apiUrl);
  }
}