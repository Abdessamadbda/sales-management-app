import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
@Injectable({
  providedIn: 'root',
})
export class WordFilesService {
  private backendBaseUrl = 'http://localhost:8080'; 

  constructor(private http: HttpClient) { }

getSalesReports(sellerId?: number): Observable<any[]> {
  const params = sellerId ? { 'sellerId': sellerId } : undefined;
  return this.http.get<any[]>(`${this.backendBaseUrl}/report`, { params });
}

}
