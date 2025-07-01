import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-course-details',
  standalone:true,
  imports: [DatePipe, RouterModule],
  templateUrl: './course-details.component.html',
  styleUrl: './course-details.component.css'
})
export class CourseDetailsComponent implements OnInit {
  course: any = {}; // Object to store course details

  constructor(private route: ActivatedRoute, private authService: AuthService,private router: Router) {}

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
  onDeleteCourse(): void {
    const confirmation = confirm(
      `Are you sure you want to delete the course "${this.course.title}"?`
    );
    if (confirmation) {
      this.authService.deleteCourse(this.course.id).subscribe({
        next: () => {
          alert('Course deleted successfully.');
          this.router.navigate(['/instructor/manage-courses']); // Redirect to manage courses
        },
        error: (err) => {
          console.error('Error deleting course:', err);
          alert('Failed to delete course. Please try again later.');
        },
      });
    }
  }
  onEditCourse(): void {
    this.router.navigate(['/instructor/courses', this.course.id, 'edit'],{
      queryParams: { courseId: this.course.id }, // Add query parameters
    });
  }

  onAddAssignment(): void {
    this.router.navigate(['/instructor/courses', this.course.id, 'add-assignment'], {
      queryParams: { courseId: this.course.id }, // Add query parameters
    });
  }

  onViewAssignments(): void {
    this.router.navigate(['/instructor/courses', this.course.id, 'view-assignments']);
  }

  onAddQuiz(): void {
    this.router.navigate(['/instructor/courses', this.course.id, 'add-quiz'], {
      queryParams: { courseId: this.course.id }, // Pass courseId as query parameter
    });
  }

  onViewQuizzes(): void {
    this.router.navigate(['/instructor/courses', this.course.id, 'view-quizzes'], {
      queryParams: { courseId: this.course.id }, // Pass courseId as query parameter
    });
  }

  onGradeAssignments(): void {
    this.router.navigate(['/instructor/courses', this.course.id, 'grade-assignments'], {
      queryParams: { courseId: this.course.id }, // Pass courseId as query parameter
    });
  }

}
