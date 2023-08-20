import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';


@Injectable({
  providedIn: 'root'
})
export class AdminserviceService {

  constructor(private http:HttpClient) {}
  getUserData(username: string,password: string){
    const loginData = { username: username, password: password };
    return this.http.post('http://localhost:8080/admin/alogin',loginData);
  }
}
