import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Seller } from './Seller.component';

@Injectable({
  providedIn: 'root'
})
export class SellersListService {
  model: any[] = [];


  constructor(private http: HttpClient) {}
  updateSeller(sellerData: any): Observable<any> {
    return this.http.post<any>(`http://localhost:8080/seller/update`, sellerData);
  }
 
  removeSeller(sellerId: number): Observable<void> {
    const url = `http://localhost:8080/seller/${sellerId}`;
    return this.http.delete<void>(url);
  }

  
  
}
