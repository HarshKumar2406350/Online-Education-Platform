import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';
import { CourseCardComponent } from '../view-courses/course-card/course-card.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-enrolled-courses',
  standalone: true,
  imports: [CommonModule, CourseCardComponent],
  templateUrl: './enrolled-courses.component.html',
  styleUrls: ['./enrolled-courses.component.css'],
})
export class EnrolledCoursesComponent implements OnInit {
  
  enrolledCourses: any[] = []; // Array to store enrolled courses

  constructor(private authService: AuthService,private router: Router) {}

  ngOnInit(): void {
    this.authService.getStudentEnrollments().subscribe({
      next: (data) => {
        this.enrolledCourses = data; // Store enrolled courses
      },
      error: (err) => {
        console.error('Error fetching enrolled courses:', err);
        alert('Failed to fetch enrolled courses. Please try again later.');
      },
    });
  }

  onCourseClick(course: any): void {
    this.router.navigate(['/student/courses', course.id, 'details'], {
      queryParams: { courseId: course.id }, // Pass courseId as query parameter
    });
  }
}