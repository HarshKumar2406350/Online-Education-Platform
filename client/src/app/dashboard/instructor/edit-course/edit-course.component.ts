import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edit-course',
  standalone: true,
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './edit-course.component.html',
  styleUrls: ['./edit-course.component.css'],
})
export class EditCourseComponent implements OnInit {
  editCourseForm!: FormGroup;
  courseId!: string;

  constructor(
    private route: ActivatedRoute,
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.paramMap.get('courseId')!;
    this.initializeForm();

    // Fetch course details to populate the form
    this.authService.getCourseDetails(this.courseId).subscribe({
      next: (course) => {
        this.editCourseForm.patchValue({
          title: course.title,
          description: course.description,
          category: course.category,
        });
      },
      error: (err) => {
        console.error('Error fetching course details:', err);
        alert('Failed to fetch course details. Please try again later.');
      },
    });
  }

  initializeForm(): void {
    this.editCourseForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required, Validators.minLength(10)]],
      category: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    if (this.editCourseForm.invalid) {
      alert('Please fill out all required fields correctly.');
      return;
    }

    const updatedCourse = this.editCourseForm.value;
    this.authService.updateCourse(this.courseId, updatedCourse).subscribe({
      next: () => {
        alert('Course updated successfully.');
        this.router.navigate(['/instructor/manage-courses']); // Redirect to manage courses
      },
      error: (err) => {
        console.error('Error updating course:', err);
        alert('Failed to update course. Please try again later.');
      },
    });
  }
}