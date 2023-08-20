import { Component } from '@angular/core';
import { Admin } from './admin';
import { AdminserviceService } from './adminservice.service';
import { Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';

@Component({
  selector: 'app-alogin',
  templateUrl: './alogin.component.html',
  styleUrls: ['./alogin.component.css']
})
export class AloginComponent {
  feedbackTimeout: any;

  login: boolean=false ;

  model: Admin = new Admin();
  getData: boolean = false;

  constructor(private adminservice: AdminserviceService, private router: Router, private authService: AuthenticationService) {}

  ngOnInit() {}

 
  loginAdmin() {
    const { username, password } = this.model;
    this.adminservice.getUserData(username, password).subscribe(
      (response: any) => {
        if (response.token && response.admin) {
          const token1 = response.token;
          const admin = response.admin;
          this.authService.setToken(token1);

          this.router.navigate(['/admin/aprofile1']);
        } else {
          this.login=true;
          this.setFeedbackTimeout(3000);
        }
      },
      (error: any) => {
        this.login=true;
        this.setFeedbackTimeout(3000);

      }
    );
  }
  setFeedbackTimeout(duration: number) {
    clearTimeout(this.feedbackTimeout); 
    this.feedbackTimeout = setTimeout(() => {
      this.login = false;

    }, duration);
  }
  

}
