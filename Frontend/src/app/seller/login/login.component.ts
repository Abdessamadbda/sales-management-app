import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserserviceService } from './userservice.service';
import { Users } from './users';
import { AuthService } from './auth.service';
import { AuthenticationService } from './authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  feedbackTimeout:any;
  login:boolean=false;
  model: Users = new Users();

  constructor(
    private userservice: UserserviceService,
    private authService: AuthService,
    private router: Router,
    private authenticationservice: AuthenticationService
  ) {}

  loginUser() {
    const { username, password } = this.model;
    this.userservice.getUserData(username, password).subscribe(
      (response: any) => {
        if (response.token && response.user) {
          const token = response.token;
          const user = response.user;
          const id=user.id;
          const nomComplet = user.nom_complet;
          const ville = user.ville;
          const phone = user.phone;
          this.authenticationservice.setToken(token);
          this.authService.setNomComplet(nomComplet);
          this.authService.setPhone(phone);
          this.authService.setVille(ville);
          this.authService.setId(String(id));

          this.router.navigate(['/seller/profile1']);
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






