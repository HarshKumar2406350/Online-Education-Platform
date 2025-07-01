import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AuthService } from '../../../../../auth/auth.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-delete-assignment',
  standalone: true,
  templateUrl: './delete-assignment.component.html',
  styleUrls: ['./delete-assignment.component.css'],
})
export class DeleteAssignmentComponent {
  @Input() assignmentId: string = ''; // Input property to receive assignment ID
  @Output() deleteResult = new EventEmitter<boolean>(); // Output event for delete result

  constructor(private authService: AuthService,private route: ActivatedRoute){}

  onConfirm(): void {
    // Call the delete API
    this.authService.deleteAssignment(this.assignmentId).subscribe({
      next: () => {
        alert('Assignment deleted successfully!');
        this.deleteResult.emit(true); // Emit true for successful deletion
      },
      error: (err) => {
        console.error('Error deleting assignment:', err);
        alert('Failed to delete assignment.');
        this.deleteResult.emit(false); // Emit false for failed deletion
      },
    });
  }

  onCancel(): void {
    this.deleteResult.emit(false); // Emit false for cancellation
  }
}