import { Component } from '@angular/core';
import { AuthenticationService } from './authentication.service';
import { AuthService } from '../login/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-bar1',
  templateUrl: './nav-bar1.component.html',
  styleUrls: ['./nav-bar1.component.css']
})
export class NavBar1Component {
  private readonly JWT_TOKEN = 'jwtToken';
  private token!: string;
   nomComplet!: string;

  constructor(private authenticationService: AuthenticationService,private authService:AuthService, private router: Router) {}

  ngOnInit() {
    this.nomComplet = this.authService.getNomComplet(); 
  }

  logoutUser() {
    this.authenticationService.clearToken();
       this.router.navigate(['/seller/login']);
  }
}
