import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-admin-course-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './admin-course-details.component.html',
  styleUrls: ['./admin-course-details.component.css'],
})
export class AdminCourseDetailsComponent implements OnInit {
  course: any = {}; // Object to store course details
  enrolledStudents: any[] = []; // Array to store enrolled students
  showEnrolledStudents: boolean = false; // Toggle visibility of enrolled students table

  constructor(private route: ActivatedRoute, private authService: AuthService) {}

  ngOnInit(): void {
    const courseId = this.route.snapshot.paramMap.get('courseId'); // Get courseId from route
    this.authService.getCourseDetails(courseId!).subscribe({
      next: (data) => {
        this.course = data; // Store course details
      },
      error: (err) => {
        console.error('Error fetching course details:', err);
        alert('Failed to fetch course details. Please try again later.');
      },
    });
  }

  toggleEnrollments(): void {
    this.showEnrolledStudents = !this.showEnrolledStudents; // Toggle visibility
    if (this.showEnrolledStudents && this.enrolledStudents.length === 0) {
      const courseId = this.course.id;
      if (!courseId) {
        console.error('Course ID is undefined.');
        alert('Failed to fetch enrollments. Course ID is missing.');
        return;
      }

      this.authService.getEnrollments(courseId).subscribe({
        next: (data) => {
          this.enrolledStudents = data; // Store enrolled students
          console.log('Enrolled Students:', this.enrolledStudents); // Debugging: Log enrolled students
        },
        error: (err) => {
          console.error('Error fetching enrollments:', err);
          alert('Failed to fetch enrollments. Please try again later.');
        },
      });
    }
  }
}