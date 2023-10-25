import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Tpe } from './Tpe.component';

@Injectable({
  providedIn: 'root'
})
export class TpeService {

  constructor(private http: HttpClient) {}
  getTpeForToday(sellerId: number): Observable<Tpe[]> {
    const url = `http://localhost:8080/aujour1/${sellerId}`;
    return this.http.get<Tpe[]>(url);
  }
  updateTpe(tpeData: any): Observable<any> {
    return this.http.post<any>(`http://localhost:8080/seller/tpe/update`, tpeData);
  }
}
