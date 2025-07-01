import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../../auth/auth.service';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DeleteAssignmentComponent } from "./delete-assignment/delete-assignment.component";

interface AssignmentResponse {
  message: string;
  statusCode: number;
  body: any[];
}



@Component({
  selector: 'app-view-assignemts',
  standalone: true,
  imports: [CommonModule, DeleteAssignmentComponent],
  templateUrl: './view-assignemts.component.html',
  styleUrls: ['./view-assignemts.component.css'],
})
export class ViewAssignemtsComponent implements OnInit {

  assignments: any[] = []; // Store assignments
  courseId!: string; // Store courseId dynamically
  showDialog = false;
  selectedAssignmentId!:string;

  constructor(private authService: AuthService, private route: ActivatedRoute,private router: Router) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.paramMap.get('courseId')!; // Get courseId from route
    this.fetchAssignments();
  }


  fetchAssignments(): void {
    this.authService.getAssignmentsByCourse(this.courseId).subscribe({
      next: (data: AssignmentResponse) => {
        this.assignments = data.body;
        console.log(this.assignments);
      },
      error: (err) => {
        console.error('Error fetching assignments:', err);
        alert('Failed to fetch assignments. Please try again later.');
      },
    });
  }
  
  

  onDeleteAssignment(assignmentId: string): void {
    this.selectedAssignmentId = assignmentId;
    this.showDialog = true;
  }
  
  handleDeleteResult(result: boolean): void {
    this.showDialog = false; // Hide the dialog box
    if (result) {
      this.fetchAssignments(); // Refresh the assignments list after successful deletion
    }
  }

  onEditAssignment(arg0: any) {
    throw new Error('Method not implemented.');
  }
}