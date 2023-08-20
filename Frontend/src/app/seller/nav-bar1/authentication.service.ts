import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private readonly JWT_TOKEN = 'jwtToken';
  constructor(private http: HttpClient, private router:Router) {}
  

clearToken(): void {
  localStorage.removeItem(this.JWT_TOKEN);
}
  
}
