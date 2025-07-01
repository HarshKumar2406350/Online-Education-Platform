import { Component, OnInit } from '@angular/core';
import { CourseCardComponent } from "./course-card/course-card.component";
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';
import { EnrollComponent } from './enroll/enroll.component';

@Component({
  selector: 'app-view-courses',
  standalone:true,
  imports: [CourseCardComponent,CommonModule,EnrollComponent],
  templateUrl: './view-courses.component.html',
  styleUrl: './view-courses.component.css'
})
export class ViewCoursesComponent implements OnInit {
  courses: any[] = []; // Array to store courses
  selectedCourseId: string = ''; // Store the ID of the selected course
  selectedCourseTitle: string = ''; // Store the title of the selected course
  showDialog: boolean = false; // Control dialog visibility

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // Fetch all courses from the backend
    this.authService.getAllCourses().subscribe({
      next: (data) => {
        this.courses = data; // Store the courses in the array
      },
      error: (err) => {
        console.error('Error fetching courses:', err);
        alert('Failed to fetch courses. Please try again later.');
      },
    });
  }

  onCourseClick(course: any): void {
    this.selectedCourseId = course.id;
    this.selectedCourseTitle = course.title;
    this.showDialog = true; // Show the dialog
  }

  onEnrollmentResult(result: boolean): void {
    this.showDialog = false; // Hide the dialog
    // if (result) {
    //   // alert(`Enrollment successful for ${this.selectedCourseTitle}`);
    // } else {
    //   alert(`Enrollment canceled or failed for ${this.selectedCourseTitle}`);
    // }
  }
}
