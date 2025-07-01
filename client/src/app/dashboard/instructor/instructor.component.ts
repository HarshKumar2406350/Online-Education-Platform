import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { HeaderComponent } from "../../home-page/header/header.component";
import { AuthService } from '../../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-instructor',
  standalone:true,
  imports: [RouterModule, HeaderComponent,CommonModule],
  templateUrl: './instructor.component.html',
  styleUrl: './instructor.component.css'
})
export class InstructorComponent {
  isPlaceholderVisible: boolean = true; 


  constructor(private authService: AuthService, private router: Router) {}


  onRouteActivate(): void {
    setTimeout(() => {
      this.isPlaceholderVisible = false; // Defer the change
    });
  }

  // Logout method
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
