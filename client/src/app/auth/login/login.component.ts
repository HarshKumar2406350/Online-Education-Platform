import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone:true,
  imports:[FormsModule,ReactiveFormsModule,CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {

  loginForm: FormGroup;
  errorMessage: string = '';
  selectedRole: string = ''; // To store the role selected by the user

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required,Validators.minLength(6)]],
      role: [''], // Role will be dynamically set based on button click
    });
  }

  // Method to handle role selection
  selectRole(role: string) {
    this.selectedRole = role;
    this.loginForm.patchValue({ role }); // Update the role in the form
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.authService.login(this.loginForm.value).subscribe({
        next: (response) => {
          // Store token and user details in localStorage
          localStorage.setItem('token', response.token);
          localStorage.setItem('role', response.user.role); // Extract role from user object
          localStorage.setItem('user', JSON.stringify(response.user)); // Store user object
  
          // Navigate based on the role
          const role = response.user.role.toUpperCase(); // Normalize role for comparison
          switch (role) {
            case 'STUDENT':
              this.router.navigate(['/student']);
              break;
            case 'INSTRUCTOR':
              this.router.navigate(['/instructor']);
              break;
            case 'ADMIN':
              this.router.navigate(['/admin']);
              break;
            default:
              this.router.navigate(['/']);
          }
          console.log(response.user.role)
        },
        error: (err) => {
          if (err.status === 400) {
            this.errorMessage = 'Invalid credentials. Please check your email, password and role.';
          }else{
            this.errorMessage = 'Login failed';
          }
        },
      });
    }
  }
}