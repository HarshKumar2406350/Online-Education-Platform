import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-grade-assignments',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './grade-assignments.component.html',
  styleUrls: ['./grade-assignments.component.css'],
})
export class GradeAssignmentsComponent implements OnInit {
  submissions: any[] = []; // Array to store assignment submissions

  constructor(private route: ActivatedRoute, private authService: AuthService) {}

  ngOnInit(): void {
    const courseId = this.route.snapshot.paramMap.get('courseId'); // Get courseId from route
    this.authService.getSubmissionsByCourse(courseId!).subscribe({
      next: (data) => {
        this.submissions = data; // Store submissions
        console.log('Submissions fetched:', this.submissions); // Debugging: Log fetched submissions
      },
      error: (err) => {
        console.error('Error fetching submissions:', err);
        alert('Failed to fetch submissions. Please try again later.');
      },
    });
  }
  
  onGradeSubmission(submissionId: string): void {
    const marks = prompt('Enter marks for the submission:');
    if (marks) {
      this.authService.gradeAssignment(submissionId, parseInt(marks, 10)).subscribe({
        next: (response) => {
          alert('Assignment graded successfully.');
          this.ngOnInit(); // Refresh the list
        },
        error: (err) => {
          console.error('Error grading assignment:', err);
          alert('Failed to grade assignment. Please try again later.');
        },
      });
    }
  }
}