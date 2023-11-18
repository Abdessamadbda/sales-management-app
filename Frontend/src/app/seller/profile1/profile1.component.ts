import { Component, OnInit } from '@angular/core';
import { AuthService } from '../login/auth.service';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { SalesService } from '../declaration/sales.service';
import * as FileSaver from 'file-saver';
@Component({
  selector: 'app-profile1',
  templateUrl: './profile1.component.html',
  styleUrls: ['./profile1.component.css']
})
export class Profile1Component implements OnInit{
  nomComplet!: string;
  ville!: string;
  phone!:string;
  agence!:string;

  animatedText = '';
  salesReports: any[] = [];

  constructor(private authService: AuthService,private router:Router,private salesService:SalesService,private route: ActivatedRoute ){}

  ngOnInit() {
    this.nomComplet = this.authService.getNomComplet(); 
    this.ville = this.authService.getVille(); 
    this.phone = this.authService.getPhone(); 
    this.agence = this.authService.getAgence(); 

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
declaration(){
this.router.navigate(['/seller/declaration'])
}
states(){
  this.router.navigate(['/seller/selectSeller'])
  }
}


