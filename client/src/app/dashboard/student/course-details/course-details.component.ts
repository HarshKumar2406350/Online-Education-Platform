import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css'],
})
export class EnrolledCourseDetailsComponent implements OnInit {
  course: any = {}; // Object to store course details
  enrollmentDate!: string; // Store enrollment date

  constructor(private authService: AuthService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    const courseId = this.route.snapshot.queryParamMap.get('courseId'); // Get courseId from query params
  
    if (courseId) {
      this.authService.getCourseDetails(courseId).subscribe({
        next: (data) => {
          this.course = data // Fetch course details from API
          this.enrollmentDate = data.enrollmentDate; // Fetch enrollment date
        },
        error: (err) => {
          console.error('Error fetching course details:', err);
          alert('Failed to load course details.');
          this.router.navigate(['/student/enrolled-courses']); // Redirect back to enrolled courses
        },
      });
    } else {
      console.error('No courseId found in query params.');
      alert('Failed to load course details.');
      this.router.navigate(['/student/enrolled-courses']); // Redirect back to enrolled courses
    }
  }

  onUnenroll(): void {
    const confirmation = confirm(`Are you sure you want to unenroll from the course "${this.course.title}"?`);
    if (confirmation) {
      this.authService.unenrollFromCourse(this.course.id).subscribe({
        next: () => {
          alert('Successfully unenrolled from the course.');
          this.router.navigate(['/student/enrolled-courses']); // Redirect to enrolled courses
        },
        error: (err) => {
          console.error('Error unenrolling from course:', err);
          alert('Failed to unenroll. Please try again later.');
        },
      });
    }
  }

  onViewAssignments(): void {
    console.log(this.course.id);
    this.router.navigate(['/student/courses', this.course.id, 'view-assignments'],{
      queryParams:{courseId : this.course.id},
    });
  }

  onViewQuizzes(): void {
    this.router.navigate(['/student/courses', this.course.id, 'view-quizzes'], {
      queryParams: { courseId: this.course.id }, // Pass courseId as query parameter
    });
  }
}