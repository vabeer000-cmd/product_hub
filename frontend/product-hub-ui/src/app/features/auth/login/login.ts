import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { Auth } from '../../../core/services/auth';
import { LoginRequest } from '../../../core/models/login-request';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  private auth = inject(Auth);
  private router = inject(Router);

  loginData: LoginRequest = {
    username: '',
    password: ''
  };

  login(): void {

    this.auth.login(this.loginData).subscribe({

      next: () => {
        this.router.navigate(['/products']);
      },

      error: error => {
        console.error('Login failed', error);
      }

    });

  }
}