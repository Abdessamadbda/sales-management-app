import { Inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { JWTGenerator } from 'src/app/jwt-generator.interface';
import { JWT_GENERATOR } from 'src/app/jwt-generator.token';
import  jwt_decode from 'jwt-decode';


@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private readonly JWT_TOKEN = 'jwtToken';
  private token!: string;

  constructor(
    private http: HttpClient,
    @Inject(JWT_GENERATOR) private jwtGenerator: JWTGenerator 
  ) {}

  logout() {
    localStorage.removeItem(this.JWT_TOKEN);
  }

  getToken(): string | null {
    return localStorage.getItem(this.JWT_TOKEN);
  }

  isAuthenticated() {
    return this.jwtGenerator.validateToken(this.getToken() || '');
  }

  setToken(token: string) {
    this.token = token;
    localStorage.setItem(this.JWT_TOKEN, token);
  }
  getUserRole(): string | null {
    const token = this.getToken();
    if (token) {
      const decodedToken = jwt_decode(token) as { role: string };
      return decodedToken.role;
    }
    return null;
  }


}
