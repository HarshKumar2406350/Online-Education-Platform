import { Routes } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { LoginComponent } from './auth/login/login.component';
import { StudentComponent } from './dashboard/student/student.component';
import { InstructorComponent } from './dashboard/instructor/instructor.component';
import { RegisterComponent } from './auth/register/register.component';
import { AuthGuard } from './auth/auth.guard';
import { UnauthorizedComponent } from './auth/Unauthorized.component';
import { AdminComponent } from './dashboard/admin/admin.component';
import { CreateCourseComponent } from './dashboard/instructor/create-course/create-course.component';
import { ManageCoursesComponent } from './dashboard/instructor/manage-courses/manage-courses.component';
import { ViewCoursesComponent } from './dashboard/student/view-courses/view-courses.component';
import { CourseDetailsComponent } from './dashboard/instructor/course-details/course-details.component';
import { EditCourseComponent } from './dashboard/instructor/edit-course/edit-course.component';
import { EnrollmentsComponent } from './dashboard/instructor/enrollments/enrollments.component';
import { AddAssignmentComponent } from './dashboard/instructor/Assignment/add-assignment/add-assignment.component';
import { ViewAssignemtsComponent } from './dashboard/instructor/Assignment/view-assignemts/view-assignemts.component';
import { DeleteAssignmentComponent } from './dashboard/instructor/Assignment/view-assignemts/delete-assignment/delete-assignment.component';
import { AddQuizComponent } from './dashboard/instructor/Assignment/add-quiz/add-quiz.component';
import { ViewQuizesComponent } from './dashboard/instructor/Assignment/view-quizes/view-quizes.component';
import { EnrolledCoursesComponent } from './dashboard/student/enrolled-courses/enrolled-courses.component';
import { EnrolledCourseDetailsComponent } from './dashboard/student/course-details/course-details.component';
import { StudentAssignmentViewComponent } from './dashboard/student/student-assignment-view/student-assignment-view.component';
import { SubmitAssignmentComponent } from './dashboard/student/submit-assignment/submit-assignment.component';
import { StudentViewQuizzesComponent } from './dashboard/student/student-view-quizzes/student-view-quizzes.component';
import { AnswerQuizComponent } from './dashboard/student/answer-quiz/answer-quiz.component';
import { ProgressComponent } from './dashboard/student/progress/progress.component';
import { ManageUsersComponent } from './dashboard/admin/manage-users/manage-users.component';
import { AdminManageCoursesComponent } from './dashboard/admin/admin-manage-courses/admin-manage-courses.component';
import { AdminCourseDetailsComponent } from './dashboard/admin/admin-course-details/admin-course-details.component';
import { GradeAssignmentsComponent } from './dashboard/instructor/grade-assignments/grade-assignments.component';

export const routes: Routes = [
    {
        path:'', // Default route
        component: HomePageComponent
    },
    {
        path:'login', 
        component:LoginComponent
    },
    {
        path:'register', 
        component:RegisterComponent
    },
    {
        path: 'student', 
        component: StudentComponent,
        children:[
            {path:'all-courses', component:ViewCoursesComponent},
            { path: 'enrolled-courses', component: EnrolledCoursesComponent },
            { path: 'courses/:courseId/details', component: EnrolledCourseDetailsComponent },
            { path: 'courses/:courseId/view-assignments', component: StudentAssignmentViewComponent },
            { path: 'courses/:courseId/submit-assignment', component: SubmitAssignmentComponent },
            { path: 'courses/:courseId/view-quizzes', component: StudentViewQuizzesComponent }, // Route for viewing quizzes
            { path: 'courses/:courseId/answer-quiz', component: AnswerQuizComponent }, // Route for answering quizzes
            { path: 'progress', component: ProgressComponent },
        
        ],
        canActivate: [AuthGuard],
        data: { role: 'STUDENT' },
      },
      {
        path: 'instructor', 
        component: InstructorComponent,
        children: [
            { path: 'create-course', component: CreateCourseComponent },
            { path: 'manage-courses', component: ManageCoursesComponent },
            { path: 'courses/:courseId', component: CourseDetailsComponent },
            { path: 'courses/:courseId/edit', component: EditCourseComponent }, 
            { path: 'enrollment-insights', component: EnrollmentsComponent },
            { path: 'courses/:courseId/add-assignment', component: AddAssignmentComponent },
            { path: 'courses/:courseId/view-assignments', component: ViewAssignemtsComponent },
            { path: 'assignments/delete' , component:DeleteAssignmentComponent},
            { path: 'courses/:courseId/add-quiz', component: AddQuizComponent }, // Added route for AddQuizComponent
            { path: 'courses/:courseId/view-quizzes', component: ViewQuizesComponent },
            { path: 'courses/:courseId/grade-assignments', component: GradeAssignmentsComponent }, // route for grading assignments
        //     { path: 'profile', component: ProfileComponent },
          ],
        canActivate: [AuthGuard],
        data: { role: 'INSTRUCTOR' },
      },
      {
        path: 'admin', 
        component: AdminComponent,
        children: [
          { path: 'manage-users', component: ManageUsersComponent },
          { path: 'manage-courses', component: AdminManageCoursesComponent },
          { path: 'courses/:courseId/details', component: AdminCourseDetailsComponent },
        ],
        canActivate: [AuthGuard],
        data: {role: 'ADMIN'},
      },
    {
        path: 'unauthorized', 
        component: UnauthorizedComponent,
    },
];
