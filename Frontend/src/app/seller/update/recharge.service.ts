import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Recharge } from './Recharge.component';

@Injectable({
  providedIn: 'root'
})
export class RechargeService {

  constructor(private http: HttpClient) {}
  getRechargeForToday(sellerId: number): Observable<Recharge[]> {
    const url = `http://localhost:8080/aujour/${sellerId}`;
    return this.http.get<Recharge[]>(url);
  }
  updateRecharge(rechargeData: any): Observable<any> {
    return this.http.post<any>(`http://localhost:8080/seller/recharge/update`, rechargeData);
  }
  
}
