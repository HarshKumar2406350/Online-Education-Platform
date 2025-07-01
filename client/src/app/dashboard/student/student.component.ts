import { Component } from '@angular/core';
import { HeaderComponent } from "../../home-page/header/header.component";
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../auth/auth.service';

@Component({
  selector: 'app-student',
  standalone:true,
  imports: [RouterModule, HeaderComponent,CommonModule],
  templateUrl: './student.component.html',
  styleUrl: './student.component.css'
})
export class StudentComponent {
  isPlaceholderVisible: boolean = true; 


  constructor(private authService: AuthService, private router: Router) {}


  onRouteActivate(): void {
    setTimeout(() => {
      this.isPlaceholderVisible = false; // Defer the change
    });
  }

  onLogout(): void {
    this.authService.logout().subscribe({
      next: () => {
        // Clear localStorage and navigate to the dashboard
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        localStorage.removeItem('user');
        this.router.navigate(['/']); // Navigate to the dashboard or home page

        // Notify the HeaderComponent to update login status
        const headerComponent = document.querySelector('app-header') as any;
        if (headerComponent && headerComponent.checkLoginStatus) {
          headerComponent.checkLoginStatus();
        }
      },
      error: (err) => {
        console.error('Logout failed:', err);
      },
    });
  }

}
