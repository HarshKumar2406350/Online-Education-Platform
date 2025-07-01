import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink,CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  isHomePage: boolean = false; // Tracks if the current route is the home page
  isLoggedIn: boolean = false; // Tracks if the user is logged in

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.isHomePage=true
    // Check if the current route is the home page
    this.router.events.subscribe(() => {
      this.isHomePage = this.router.url === '/';
    });

    // Check login status dynamically
    this.checkLoginStatus();
  }

  checkLoginStatus(): void {
    this.isLoggedIn = !!localStorage.getItem('token'); // Check if token exists
  }
}
