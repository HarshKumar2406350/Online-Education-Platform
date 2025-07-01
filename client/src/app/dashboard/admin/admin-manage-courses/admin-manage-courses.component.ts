import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/auth.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CourseCardComponent } from '../../instructor/manage-courses/course-card/course-card.component';

@Component({
  selector: 'app-manage-courses',
  standalone: true,
  imports: [CommonModule, RouterModule,CourseCardComponent],
  templateUrl: './admin-manage-courses.component.html',
  styleUrls: ['./admin-manage-courses.component.css'],
})
export class AdminManageCoursesComponent implements OnInit {
  courses: any[] = []; // Store the list of courses

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.fetchCourses();
  }

  // Fetch all courses
  fetchCourses(): void {
    this.authService.getAllCourses().subscribe({
      next: (response) => {
        this.courses = response; // Store the list of courses
        console.log('Fetched Courses:', this.courses); // Debugging: Log fetched courses
      },
      error: (err) => {
        console.error('Error fetching courses:', err); // Log error for debugging
        alert('Failed to fetch courses. Please try again later.'); // Notify the user
      },
    });
  }

  // Navigate to course details
  onCourseClick(courseId: string): void {
    this.router.navigate([`/admin/courses/${courseId}/details`]); // Include 'instructor' in the path
  }
}