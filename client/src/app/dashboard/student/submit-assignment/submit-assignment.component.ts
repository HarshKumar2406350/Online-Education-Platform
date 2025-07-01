import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-submit-assignment',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule,FormsModule],
  templateUrl: './submit-assignment.component.html',
  styleUrls: ['./submit-assignment.component.css'],
})
export class SubmitAssignmentComponent {
  @Input() assignmentId!: string; // Input property for assignment ID
  @Input() courseId!: string; // Input property for course ID
  @Output() submissionResult = new EventEmitter<boolean>(); // Output event for submission result

  answer: string = ''; // Store the student's answer

  constructor(private authService: AuthService,private route: ActivatedRoute, private router: Router) {}
  ngOnInit(): void {
    this.assignmentId = this.route.snapshot.queryParamMap.get('assignmentId')!; // Get assignmentId from query params
    this.courseId = this.route.snapshot.queryParamMap.get('courseId')!; // Get courseId from query params
  }

  onSubmit(): void {
    if (this.answer.trim().length < 10) {
      alert('Answer must be at least 10 characters long.');
      return;
    }

    const user = JSON.parse(localStorage.getItem('user')!); // Parse the user object from localStorage
    const request = {
      studentId: user.email, // Retrieve email from the parsed user object
      courseId: this.courseId,
      assignmentId: this.assignmentId,
      answer: this.answer,
    };

    this.authService.submitAssignment(request).subscribe({
      next: (response) => {
        alert(response.message || 'Assignment submitted successfully.');
        this.submissionResult.emit(true); // Emit true for successful submission
        this.router.navigate(['/student/enrolled-courses']);
      },
      error: (err) => {
        console.error('Error submitting assignment:', err);
        const errorMessage = err?.error?.message || 'Failed to submit the assignment.';
        alert(errorMessage); // Display the error message
        this.submissionResult.emit(false); // Emit false for failed submission
      },
    });
  }

  onCancel(): void {
      this.router.navigate(['/student/enrolled-courses']); // Navigate back to enrolled courses
  }
}