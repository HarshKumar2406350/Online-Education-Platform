import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';
import {type UserResponse } from './UserResponse'; // Import the UserResponse interface

@Component({
  selector: 'app-manage-users',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './manage-users.component.html',
  styleUrls: ['./manage-users.component.css'],
})
export class ManageUsersComponent implements OnInit {
  FetchedUsers: any[] = []; // Store the list of users

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.fetchUsers();
  }

  // Fetch all users
  fetchUsers(): void {
    this.authService.getAllUsers().subscribe({
      next: (response: UserResponse) => { // Use the correct type for the response
        this.FetchedUsers = response.users; // Extract the 'users' array from the response
        console.log('Fetched Users:', this.FetchedUsers); // Debugging: Log fetched users
      },
      error: (err) => {
        console.error('Error fetching users:', err); // Log error for debugging
        alert('Failed to fetch users. Please try again later.'); // Notify the user
      },
    });
  }

  
  // Delete user by ID
  deleteUser(userId: string): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.authService.deleteUserById(userId).subscribe({
        next: () => {
          alert(`User with ID ${userId} has been deleted successfully.`); // Notify the user
          this.fetchUsers(); // Refresh the user list after deletion
        },
        error: (err) => {
          console.error('Error deleting user:', err); // Log error for debugging
          alert('Failed to delete user. Please try again later.'); // Notify the user
        },
      });
    }
  }
}