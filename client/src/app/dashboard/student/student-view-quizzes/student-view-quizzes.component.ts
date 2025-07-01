import { Component } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { AuthService } from '../../../auth/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-student-view-quizzes',
  standalone:true,
  imports: [CommonModule],
  templateUrl: './student-view-quizzes.component.html',
  styleUrl: './student-view-quizzes.component.css'
})
export class StudentViewQuizzesComponent {
  quizzes: any[] = []; // Array to store quizzes
  courseId!: string; // Store courseId dynamically

  constructor(private route: ActivatedRoute, private authService: AuthService,private router: Router) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.queryParamMap.get('courseId')!;
  
    if (!this.courseId) {
      console.error('Course ID is null or undefined. Ensure it is passed as a query parameter.');
      alert('Failed to load quizzes.');
      return;
    }
  
    this.fetchQuizzes();
  }

  fetchQuizzes(): void {
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

  onAnswerQuiz(quizId: string): void {
    this.router.navigate(['/student/courses', this.courseId, 'answer-quiz'], {
      queryParams: { quizId: quizId, courseId: this.courseId }, // Pass quizId and courseId as query parameters
    });
  }


}
