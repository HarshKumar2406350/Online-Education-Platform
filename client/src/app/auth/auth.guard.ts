import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private router: Router) {}
  canActivate(route: ActivatedRouteSnapshot): boolean {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const expectedRole = route.data['role'];

  
    if (token && role) {
      if (role === expectedRole) {
        return true;
      } else {
        this.router.navigate(['/unauthorized']);
        return false;
      }
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
  


//   canActivate(route: ActivatedRouteSnapshot): boolean {
//     const token = localStorage.getItem('token');
//     const role = localStorage.getItem('role');

//     if (token && role) {
//       const expectedRole = route.data['role']; // Get the expected role from route data

//       if (role === expectedRole) {
//         return true; // Allow access if roles match
//       } else {
//         this.router.navigate(['/unauthorized']); // Redirect unauthorized users
//         return false;
//       }
//     } else {
//       this.router.navigate(['/login']); // Redirect unauthenticated users
//       return false;
//     }
//   }
// }