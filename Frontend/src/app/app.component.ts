import { Component } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'Frontend';
  constructor(private route: ActivatedRoute, private router: Router) {}

  isHomeSpace(): boolean {
    const currentRoute = this.router.url;
    if (currentRoute === '/') {
      return true;
    } else {
      return false;
    }
  }
  isSellerSpace(): boolean {
    const currentRoute = this.router.url;
    if (currentRoute === '/seller/login') {
      return true;
    } else {
      return false;
    }
  }
  isAdminSpace(): boolean {
    const currentRoute = this.router.url;
    if (currentRoute === '/admin/alogin') {
      return true;
    } else {
      return false;
    }
  }
  isAdminProfileSpace(): boolean {
    const currentRoute = this.router.url;
    if (currentRoute === '/admin/aprofile1'  ) {
      return true;
    } else {
      return false;
    }
  }
  isStatesSpace(): boolean {
    const currentRoute = this.router.url;
    const queryParams = this.route.snapshot.queryParamMap;
    const sellerQueryParam = queryParams.get('id');
  
      if (sellerQueryParam !== null) {
        return true; 
      } else {
        return false; 
      }
    
  
  }
  isNewsellerSpace(): boolean {
    const currentRoute = this.router.url;
    if (currentRoute === '/admin/newseller') {
      return true;
    } else {
      return false;
    }
  }
  isSellerProfileSpace(): boolean {
    const currentRoute = this.router.url;
    if (currentRoute === '/seller/profile1' ) {
      return true;
    } else {
      return false;
    }
  }
  isDeclarationSpace(): boolean {
    const currentRoute = this.router.url;
    if (currentRoute === '/seller/declaration' ) {
      return true;
    } else {
      return false;
    }
  }
  isUpdateSpace(): boolean {
    const currentRoute = this.router.url;
    if (currentRoute === '/seller/update' ) {
      return true;
    } else {
      return false;
    }
  }
  isSstatesSpace(): boolean {
    const currentRoute = this.router.url;
const queryParams = this.route.snapshot.queryParamMap;
const sellerQueryParam = queryParams.get('id1');

if (sellerQueryParam !== null ) {
  return true; 
} else {
  return false; 
}
  }
  isSelectSellerSpace():boolean{
    const currentRoute = this.router.url;
    if (currentRoute === '/seller/selectSeller' ) {
      return true;
    } else {
      return false;
    }

  }
  isSellersSpace(): boolean {
    const currentRoute = this.router.url;
    if (currentRoute === '/admin/sellers' ) {
      return true;
    } else {
      return false;
    }
  }
}

