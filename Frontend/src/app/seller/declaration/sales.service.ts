import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Sale } from './Sale.component';
import { AuthenticationService } from '../login/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class SalesService {
  constructor(private http: HttpClient) {}

  private apiUrl = 'http://localhost:8080/declaration'; 

  submitSales(salesData: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, salesData);
  }
  
  generateSalesReport(sellerId: number): Observable<Blob> {
    const url = `http://localhost:8080/generate-sales-report/${sellerId}`;
    return this.http.get<Blob>(url, { responseType: 'blob' as 'json' });
  }

getSalesReports(sellerId?: number): Observable<any[]> {
  const params = sellerId ? { 'sellerId': sellerId } : undefined;
  return this.http.get<any[]>(`http://localhost:8080/sales-report`, { params });
}

  

}
