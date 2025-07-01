import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CourseCardComponent } from './course-card/course-card.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-manage-courses',
  standalone: true,
  imports: [CourseCardComponent,FormsModule,CommonModule],
  templateUrl: './manage-courses.component.html',
  styleUrls: ['./manage-courses.component.css'],
})
export class ManageCoursesComponent implements OnInit {
  courses: any[] = []; // Array to store courses

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    // Fetch instructor-specific courses using AuthService
    this.authService.getInstructorCourses().subscribe({
      next: (data) => {
        this.courses = data; // Store the courses in the array
      },
      error: (err) => {
        console.error('Error fetching courses:', err);
        alert('Failed to fetch courses. Please try again later.');
      },
    });
  }

  onCourseClick(courseId: string): void {
    this.router.navigate([`/instructor/courses/${courseId}`]); // Include 'instructor' in the path
  }
}