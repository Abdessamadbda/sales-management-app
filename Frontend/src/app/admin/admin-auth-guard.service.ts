import { Injectable } from '@angular/core';
import { AuthenticationService } from './alogin/authentication.service';
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminAuthGuardService {

  constructor(private authService: AuthenticationService, private router: Router) {}

  canActivate(
    
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      const isAdmin = this.authService.getAdminRole() == 'admin';
    if (this.authService.isAuthenticated() && isAdmin) {
      return true;
    } else {
      this.router.navigate(['/admin/alogin']);
      return false;
    }
  }
}
