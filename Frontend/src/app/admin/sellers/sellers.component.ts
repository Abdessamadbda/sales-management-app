import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sellers',
  templateUrl: './sellers.component.html',
  styleUrls: ['./sellers.component.css']
})
export class SellersComponent implements OnInit{
  usersData!: any[];
  constructor( private router: Router,private http: HttpClient) {};
  ngOnInit() {
    this.fetchSalesData();
  }
  divClicked2(user: any) {
      const queryParams = { id: user.id };
      this.router.navigate(['admin/states'], { queryParams: queryParams });
  }  
  fetchSalesData() {
    this.http.get<any[]>('http://localhost:8080/admin/sellers').subscribe(
      (data) => {
        this.usersData = data;
      },
      (error) => {
        console.error('Error fetching sales data:', error);
      }
    );
  }
}
