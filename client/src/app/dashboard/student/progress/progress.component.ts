import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../auth/auth.service';
import { Chart, registerables } from 'chart.js'; // Import registerables
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-progress',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './progress.component.html',
  styleUrls: ['./progress.component.css'],
})
export class ProgressComponent implements OnInit {
  enrolledCourses: any[] = []; // Store enrolled courses
  marksData: any = {}; // Store marks per quizId (null for not taken)
  correctAnswers: number = 0; // Count of correct answers
  incorrectAnswers: number = 0; // Count of incorrect answers
  quizzesNotTaken: number = 0; // Count of quizzes not taken
  chartInstances: { [key: number]: Chart } = {}; // Track chart instances by course index

  constructor(private authService: AuthService) {
    Chart.register(...registerables); // Register Chart.js components
  }

  ngOnInit(): void {
    this.fetchEnrolledCourses();
  }

  // Fetch all enrolled courses
  fetchEnrolledCourses(): void {
    const user = JSON.parse(localStorage.getItem('user')!); // Parse user object from localStorage
    this.authService.getStudentEnrollments().subscribe({
      next: (courses) => {
        this.enrolledCourses = courses; // Store enrolled courses
        console.log('Enrolled Courses:', this.enrolledCourses); // Debugging: Log enrolled courses
        this.fetchQuizzes(user.email); // Fetch quizzes for each course
      },
      error: (err) => {
        console.error('Error fetching enrolled courses:', err);
        alert('Failed to fetch enrolled courses. Please try again later.');
      },
    });
  }

  // Fetch quizzes for each enrolled course
  fetchQuizzes(studentId: string): void {
    this.enrolledCourses.forEach((course, index) => {
      const courseId = course.courseId; // Use the actual courseId from the course object
      this.authService.getQuizzesByCourse(courseId).subscribe({
        next: (quizzes) => {
          console.log(`Quizzes for Course ${courseId}:`, quizzes); // Debugging: Log quizzes
          quizzes.forEach((quiz) => {
            this.fetchMarks(studentId, courseId, quiz.id, index); // Pass course index to fetchMarks
          });
        },
        error: (err) => {
          console.error(`Error fetching quizzes for Course ${courseId}:`, err);
        },
      });
    });
  }

  // Fetch marks for each quiz
  fetchMarks(studentId: string, courseId: string, quizId: string, courseIndex: number): void {
    this.authService.getMarksByStudentIdAndCourseIdAndQuizId(studentId, courseId, quizId).subscribe({
      next: (response: any) => {
        console.log(response); // Debugging: Log the entire response
        const marks = response.body; // Extract the 'body' value from the API response
  
        // Initialize course-specific data structure if not already present
        if (!this.marksData[courseIndex]) {
          this.marksData[courseIndex] = { correct: 0, incorrect: 0, notTaken: 0 };
        }
  
        // Update counts based on marks
        if (marks === 1) {
          this.marksData[courseIndex].correct++;
        } else if (marks === 0) {
          this.marksData[courseIndex].incorrect++;
        } else if (marks === null) {
          this.marksData[courseIndex].notTaken++;
        }
  
        console.log(`Marks for Quiz ${quizId} in Course ${courseId}:`, marks); // Debugging: Log marks
        console.log(`Updated Marks Data for Course ${courseIndex}:`, this.marksData[courseIndex]); // Debugging: Log course-specific data
  
        this.renderChart(courseIndex); // Render chart for the specific course
      },
      error: (err) => {
        console.error(`Error fetching marks for Quiz ${quizId}:`, err);
      },
    });
  }

  // Render the pie chart
  renderChart(courseIndex: number): void {
    const ctx = document.getElementById(`progressChart-${courseIndex}`) as HTMLCanvasElement;
    if (!ctx) {
      console.error(`Canvas element for chart ${courseIndex} not found.`);
      return;
    }
  
    const courseData = this.marksData[courseIndex];
    if (!courseData) {
      console.error(`No data found for Course ${courseIndex}.`);
      return;
    }
  
    // Destroy existing chart instance if it exists
    if (this.chartInstances[courseIndex]) {
      this.chartInstances[courseIndex].destroy();
    }
  
    // Create a new chart instance and store it
    this.chartInstances[courseIndex] = new Chart(ctx, {
      type: 'pie',
      data: {
        labels: ['Correct Answers', 'Incorrect Answers', 'Quizzes Not Taken'],
        datasets: [
          {
            data: [courseData.correct, courseData.incorrect, courseData.notTaken],
            backgroundColor: ['#2ecc71', '#e74c3c', '#f1c40f'], // Green for correct, red for incorrect, yellow for not taken
          },
        ],
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'bottom',
          },
        },
      },
    });
  }
}