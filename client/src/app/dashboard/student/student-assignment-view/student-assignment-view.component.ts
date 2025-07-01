import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';



interface AssignmentResponse {
  message: string;
  statusCode: number;
  body: any[];
}

@Component({
  selector: 'app-student-assignment-view',
  standalone: true,
  imports:[CommonModule],
  templateUrl: './student-assignment-view.component.html',
  styleUrls: ['./student-assignment-view.component.css'],
})
export class StudentAssignmentViewComponent implements OnInit {
  assignments: any[] = []; // Array to store assignments
  courseId!: string; // Store courseId dynamically

  constructor(private route: ActivatedRoute, private authService: AuthService,private router: Router) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.paramMap.get('courseId')!; // Get courseId from route
    this.fetchAssignments();
  }

  fetchAssignments(): void {
    this.authService.getAssignmentsByCourse(this.courseId).subscribe({
      next: (data: AssignmentResponse) => {
        this.assignments = data.body;
        console.log(this.assignments);
      },
      error: (err) => {
        console.error('Error fetching assignments:', err);
        alert('Failed to fetch assignments. Please try again later.');
      },
    });
  }
  

  onSubmitAnswer(assignmentId: string): void {
    
    this.router.navigate(['/student/courses', this.courseId, 'submit-assignment'], {
      queryParams: { assignmentId: assignmentId, courseId: this.courseId }, // Pass assignmentId and courseId as query parameters
    });
  }
}