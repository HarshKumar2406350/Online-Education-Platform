import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../auth/auth.service';

@Component({
  selector: 'app-create-course',
  standalone: true,
  imports:[ReactiveFormsModule,CommonModule],
  templateUrl: './create-course.component.html',
  styleUrls: ['./create-course.component.css'],
})
export class CreateCourseComponent {
  createCourseForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.createCourseForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      category: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.createCourseForm.invalid) {
      alert('Please fill out all required fields.');
      return;
    }

    const courseData = this.createCourseForm.value;
    console.log('Course Data:', courseData);

    // Call the createCourse() method from AuthService
    this.authService.createCourse(courseData).subscribe({
      next: () => {
        alert('Course created successfully!');
        this.createCourseForm.reset(); // Reset the form after submission
      },
      error: (err) => {
        if (err.status === 403) {
          alert('You are not authorized to perform this action.');
        } else {
          console.error('Error creating course:', err);
          alert('Failed to create course. Please try again.');
        }
      },
    });
  }
}