import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AuthService } from '../../../../auth/auth.service';

@Component({
  selector: 'app-enroll',
  standalone:true,
  templateUrl: './enroll.component.html',
  styleUrls: ['./enroll.component.css'],
})
export class EnrollComponent {
  @Input() courseId: string = ''; // Input property to receive course ID
  @Input() courseTitle: string = ''; // Input property to receive course title
  @Output() enrollmentResult = new EventEmitter<boolean>(); // Output event for enrollment result

  constructor(private authService: AuthService) {}

  onConfirm(): void {
    // Call the enrollment API
    this.authService.enrollInCourse(this.courseId).subscribe({
      next: () => {
        alert(`Successfully enrolled in ${this.courseTitle}`);
        this.enrollmentResult.emit(true); // Emit true for successful enrollment
      },
      error: (err) => {
        console.error('Error enrolling in course:', err);
        const errorMessage = err?.error?.message || 'Failed to enroll in the course'; // Extract error message
        alert(errorMessage); // Display the error message
        this.enrollmentResult.emit(false); // Emit false for failed enrollment
      },
    });
  }

  onCancel(): void {
    this.enrollmentResult.emit(false); // Emit false for cancellation
  }
}