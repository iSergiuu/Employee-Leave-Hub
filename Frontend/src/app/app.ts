import { Component } from '@angular/core';
import { Router, NavigationEnd, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { filter } from 'rxjs/operators';
import { SidebarComponent } from './layout/sidebar/sidebar'; 

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, SidebarComponent], 
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class App {
  showSidebar = false; 

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      const hiddenRoutes = ['/login', '/register', '/'];
      const currentUrl = event.urlAfterRedirects.split('?')[0];
      this.showSidebar = !hiddenRoutes.includes(currentUrl);
    });
  }
}