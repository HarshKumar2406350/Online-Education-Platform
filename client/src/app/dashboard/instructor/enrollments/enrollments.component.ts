import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-enrollments',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './enrollments.component.html',
  styleUrls: ['./enrollments.component.css'],
})
export class EnrollmentsComponent implements OnInit {
  enrollments: any[] = [];
  sortedBy: 'courseId' | 'studentId' = 'courseId'; // Default sorting criteria
  rowColors: string[] = []; // Array to store random row colors

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.fetchCoursesAndEnrollments();
  }

  fetchCoursesAndEnrollments(): void {
    this.authService.getInstructorCourses().subscribe({
      next: (courses) => {
        const courseIds = courses.map((course) => course.id); // Extract course IDs
        this.fetchEnrollmentsForCourses(courseIds);
      },
      error: (err) => {
        console.error('Error fetching courses:', err);
        alert('Failed to fetch courses. Please try again later.');
      },
    });
  }

  fetchEnrollmentsForCourses(courseIds: string[]): void {
    const enrollmentRequests = courseIds.map((courseId) =>
      this.authService.getEnrollments(courseId)
    );
    

    Promise.all(enrollmentRequests.map((req) => req.toPromise()))
      .then((responses) => {
        this.enrollments = responses.flat(); // Combine all enrollments into a single array
        this.applyRowColors();
      })
      .catch((err) => {
        console.error('Error fetching enrollments:', err);
        alert('Failed to fetch enrollments. Please try again later.');
      });
  }

  toggleSort(): void {
    this.sortedBy = this.sortedBy === 'courseId' ? 'studentId' : 'courseId';
    this.enrollments.sort((a, b) =>
      this.sortedBy === 'courseId'
        ? a.courseId.localeCompare(b.courseId)
        : a.studentId.localeCompare(b.studentId)
    );
    this.applyRowColors();
  }

  // applyRowColors(): void {
  //   const uniqueGroups = new Set(
  //     this.enrollments.map((enrollment) =>
  //       this.sortedBy === 'courseId' ? enrollment.courseId : enrollment.studentId
  //     )
  //   );
  
  //   const groupColors = Array.from(uniqueGroups).reduce((acc, group) => {
  //     const hue = Math.floor(Math.random() * 180); // Restrict hue to 0-180 (cool colors)
  //     const saturation = 70 + Math.floor(Math.random() * 30); // Saturation between 70-100%
  //     const lightness = 50 + Math.floor(Math.random() * 20); // Lightness between 50-70%
  //     acc[group] = `hsl(${hue}, ${saturation}%, ${lightness}%)`; // Generate HSL color
  //     return acc;
  //   }, {} as Record<string, string>);
  
  //   this.rowColors = this.enrollments.map((enrollment) =>
  //     groupColors[this.sortedBy === 'courseId' ? enrollment.courseId : enrollment.studentId]
  //   );
  // }
  applyRowColors(): void {
    const coolColors = [
      '#D1C4E9', // Lavender
      '#FFE0B2', // Peach
      '#DCEDC8', // Lime
      '#F8BBD0', // Pink
      '#F0F4C3', // Yellow-Green
      '#D1C4E9', // Purple
      '#FFCCBC', // Coral
      '#B2EBF2', // Cyan
      '#BBDEFB', // Blue
      '#C8E6C9', // Green
    ];
  

    const uniqueGroups = new Set(
      this.enrollments.map((enrollment) =>
        this.sortedBy === 'courseId' ? enrollment.courseId : enrollment.studentId
      )
    );
  
    const groupColors = Array.from(uniqueGroups).reduce((acc, group, index) => {
      acc[group] = coolColors[index % coolColors.length]; // Assign colors cyclically
      return acc;
    }, {} as Record<string, string>);
  
    this.rowColors = this.enrollments.map((enrollment) =>
      groupColors[this.sortedBy === 'courseId' ? enrollment.courseId : enrollment.studentId]
    );
  }
}