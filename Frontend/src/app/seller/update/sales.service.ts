import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Sale } from './Sale.component';

@Injectable({
  providedIn: 'root'
})
export class SalesService {

  constructor(private http: HttpClient) {}
  updateSale(saleData: any): Observable<any> {
    return this.http.post<any>(`http://localhost:8080/seller/sales/update`, saleData);
  }
  getSalesForToday(sellerId: number): Observable<Sale[]> {
    const url = `http://localhost:8080/today/${sellerId}`;
    return this.http.get<Sale[]>(url);
  }
  removeSale(saleId: number): Observable<void> {
    const url = `http://localhost:8080/seller/declaration/${saleId}`;
    return this.http.delete<void>(url);
  }
  
}
