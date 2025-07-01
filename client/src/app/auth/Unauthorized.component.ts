import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports:[RouterLink],
  template: `
    <div class="unauthorized-container">
      <h2>Unauthorized Access</h2>
      <p>You do not have permission to view this page.</p>
      <a routerLink="/login" class="back-link">Go to Login</a>
    </div>
  `,
  styles: [
    `
      .unauthorized-container {
        text-align: center;
        margin: 2rem auto;
        padding: 2rem;
        max-width: 40rem;
        background: linear-gradient(180deg, #253c3c, #1d4949);
        border-radius: 8px;
        box-shadow: 0 0 20px 2px rgba(0, 0, 0, 0.6);
        color: #d9e2f1;
      }

      h2 {
        font-size: 2rem;
        margin-bottom: 1rem;
      }

      p {
        font-size: 1.2rem;
        margin-bottom: 2rem;
      }

      .back-link {
        display: inline-block;
        padding: 0.75rem 1.5rem;
        font-size: 1rem;
        color: #ffffff;
        background-color: #1a5e5e;
        border-radius: 6px;
        text-decoration: none;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
      }

      .back-link:hover {
        background-color: #147b73;
      }
    `,
  ],
})
export class UnauthorizedComponent {}