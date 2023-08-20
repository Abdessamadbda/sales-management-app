import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../login/auth.service';

@Component({
  selector: 'app-selectSeller',
  templateUrl: './selectSeller.component.html',
  styleUrls: ['./selectSeller.component.css']
})
export class SelectSellerComponent {
  ville:string='';
  nomComplet:string='';
  sellerId:number=0
  usersData!: any[];
  constructor( private authService: AuthService,private router: Router,private http: HttpClient) {};
  ngOnInit() {
    this.sellerId = this.authService.getId(); 
    this.fetchSalesData(this.sellerId);
  }
  divClicked(user: any) {
      const queryParams = { id1: user.id };
      this.router.navigate(['seller/sstates'], { queryParams: queryParams });
  }  
  fetchSalesData(sellerId:number) {
    this.http.get<any[]>(`http://localhost:8080/seller/sellers/${sellerId}`).subscribe(
      (data) => {
        this.usersData = data;
      },
      (error) => {
        console.error('Error fetching sales data:', error);
      }
    );
  }
}
