import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-view-quizes',
  imports:[CommonModule],
  standalone: true,
  templateUrl: './view-quizes.component.html',
  styleUrls: ['./view-quizes.component.css'],
})
export class ViewQuizesComponent implements OnInit {
  quizzes: any[] = []; // Array to store quizzes
  courseId!: string; // Store courseId dynamically

  constructor(private route: ActivatedRoute, private authService: AuthService) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.queryParamMap.get('courseId')!; // Get courseId from query params

    this.authService.getQuizzesByCourse(this.courseId).subscribe({
      next: (data) => {
        this.quizzes = data; // Store quizzes
      },
      error: (err) => {
        console.error('Error fetching quizzes:', err);
        alert('Failed to fetch quizzes. Please try again later.');
      },
    });
  }

  onDeleteQuiz(quizId: string): void {
    if (confirm('Are you sure you want to delete this quiz?')) {
      this.authService.deleteQuiz(quizId).subscribe({
        next: () => {
          alert('Quiz deleted successfully!');
          this.quizzes = this.quizzes.filter((quiz) => quiz.id !== quizId); // Remove deleted quiz from the list
        },
        error: (err) => {
          console.error('Error deleting quiz:', err);
          alert('Failed to delete quiz. Please try again later.');
        },
      });
    }
  }
}