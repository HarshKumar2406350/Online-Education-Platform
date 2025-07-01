import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../../auth/auth.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-add-assignment',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './add-assignment.component.html',
  styleUrls: ['./add-assignment.component.css'],
  encapsulation: ViewEncapsulation.Emulated, // Default encapsulation to isolate styles
})
export class AddAssignmentComponent implements OnInit {
  assignmentForm!: FormGroup;
  courseId: any;

  constructor(private fb: FormBuilder, private authService: AuthService,private route: ActivatedRoute) {}

  ngOnInit(): void {
    
    this.courseId = this.route.snapshot.paramMap.get('courseId')!;
    console.log('Retrieved courseId:', this.courseId); // Debugging


    this.assignmentForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      totalMarks: [0, [Validators.required, Validators.min(0)]],
      dueDate: ['', [Validators.required]],
      sequenceNo: [1, [Validators.required, Validators.min(1)]],
    });
  }

  onSubmit(): void {
    if (this.assignmentForm.invalid) {
      return;
    }
    const assignmentData = {
      courseId: this.courseId, // Bind courseId dynamically
      ...this.assignmentForm.value,
    };

    this.assignmentForm.value;
    this.authService.createAssignment(assignmentData).subscribe({
      next: (response) => {
        alert('Assignment created successfully!');
        this.assignmentForm.reset();
      },
      error: (err) => {
        console.error('Error creating assignment:', err);
        alert('Failed to create assignment. Please try again later.');
      },
    });
  }
}
