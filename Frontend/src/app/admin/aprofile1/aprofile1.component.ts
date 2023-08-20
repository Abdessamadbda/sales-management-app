import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../alogin/authentication.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-aprofile1',
  templateUrl: './aprofile1.component.html',
  styleUrls: ['./aprofile1.component.css']
})
export class Aprofile1Component implements OnInit{
  animatedText = '';
  nomComplet = 'Nabil Ghaly';

  constructor(private authService: AuthenticationService, private router: Router) {};
  ngOnInit() {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/admin/alogin']);
    }
    this.animateName();

  }
  animateName() {
    let index = 0;

    const animationInterval = setInterval(() => {
      this.animatedText = this.animatedText + this.nomComplet[index];
      index++;

      if (index >= this.nomComplet.length) {
        clearInterval(animationInterval);
      }
    }, 100); 
}

  logoutAdmin() {
    this.authService.logout();
    this.router.navigate(['/admin/alogin']);
  }
  divClicked1(){
    this.router.navigate(['/admin/newseller']);
  }
  divClicked2(){
    this.router.navigate(['/admin/sellers']);
  }
}
