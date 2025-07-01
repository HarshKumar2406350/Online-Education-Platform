import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-answer-quiz',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './answer-quiz.component.html',
  styleUrls: ['./answer-quiz.component.css'],
})
export class AnswerQuizComponent implements OnInit {
  quiz: any = {}; // Store quiz details
  courseId!: string; // Store courseId dynamically
  selectedAnswer: string = ''; // Store selected answer

  constructor(private route: ActivatedRoute, private authService: AuthService,private router: Router) {}

  ngOnInit(): void {
    const quizId = this.route.snapshot.queryParamMap.get('quizId')!;
    this.courseId = this.route.snapshot.queryParamMap.get('courseId')!;
    this.fetchQuizDetails(quizId);
  }

  fetchQuizDetails(quizId: string): void {
    this.authService.getQuizDetails(quizId).subscribe({
      next: (data) => {
        this.quiz = data; // Store quiz details
      },
      error: (err) => {
        console.error('Error fetching quiz details:', err);
        alert('Failed to fetch quiz details. Please try again later.');
      },
    });
  }

  onSubmitAnswer(): void {
    const user = JSON.parse(localStorage.getItem('user')!); // Parse user object from localStorage
    const request = {
      studentId: user.email, // Retrieve email from user object
      courseId: this.courseId,
      quizId: this.quiz.id,
      answerOption: this.selectedAnswer,
    };

    this.authService.submitQuiz(request).subscribe({
      next: (response) => {
        alert(response.message || 'Quiz submitted successfully.');
        this.router.navigate(['/student/enrolled-courses']); // Navigate back to quizzes page
      },
      error: (err) => {
        console.error('Error submitting quiz:', err);
        alert(err?.error?.message || 'Failed to submit the quiz.');
        this.router.navigate(['/student/enrolled-courses']); // Navigate back to quizzes page
      },
    });
  }
}