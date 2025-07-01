import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../../auth/auth.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-quiz',
  standalone: true,
  imports:[ReactiveFormsModule,CommonModule],
  templateUrl: './add-quiz.component.html',
  styleUrls: ['./add-quiz.component.css'],
})
export class AddQuizComponent implements OnInit {
  quizForm!: FormGroup; // Form group for quiz
  courseId!: string; // Store courseId dynamically

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.queryParamMap.get('courseId')!; // Get courseId from query params
  
    this.quizForm = this.fb.group({
      question: ['', [Validators.required, Validators.minLength(5)]],
      answerOptions: this.fb.array([
        this.fb.control('', Validators.required),
        this.fb.control('', Validators.required),
        this.fb.control('', Validators.required),
        this.fb.control('', Validators.required),
      ]),
      correctAnswer: ['', Validators.required],
    });
  }

  onSubmit(): void {
    if (this.quizForm.invalid) {
      alert('Please fill out all required fields.');
      return;
    }

    const quizRequest = {
      ...this.quizForm.value,
      courseId: this.courseId, // Bind courseId dynamically
    };

    this.authService.createQuiz(quizRequest).subscribe({
      next: (response) => {
        alert('Quiz created successfully!');
        this.quizForm.reset(); // Reset the form
      },
      error: (err) => {
        console.error('Error creating quiz:', err);
        alert('Failed to create quiz. Please try again later.');
      },
    });
  }
}