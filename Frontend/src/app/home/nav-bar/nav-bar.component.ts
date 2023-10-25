import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  activeLink!: string;
  mobileMenuVisible: boolean = false;

  constructor(private router: Router) {}

  ngOnInit(): void {
    const lastClickedLink = localStorage.getItem('lastClickedLink');
    this.activeLink = lastClickedLink || 'link1';

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const currentUrl = this.router.url;
        if (currentUrl === '/') {
          this.activeLink = 'link1';
        }
      }
    });
  }

  handleLinkClick(link: string): void {
    this.activeLink = link;
    localStorage.setItem('lastClickedLink', link);
  }
  

  toggleMobileMenu() {
    this.mobileMenuVisible = !this.mobileMenuVisible;
  }
}


