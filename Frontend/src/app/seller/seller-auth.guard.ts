import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticationService } from './login/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class sellerAuthGuard implements CanActivate {
  constructor(private authService: AuthenticationService, private router: Router) {}

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const isUser = this.authService.getUserRole() === 'user';
      if (this.authService.isAuthenticated() && isUser) {
      return true;
    } else {
      this.router.navigate(['/seller/login']);
      return false;
    }
  }
}

