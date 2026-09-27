import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { Auth } from '../../../core/services/auth';
import { RegisterRequest } from '../../../core/models/register-request';

@Component({
  selector: 'app-register',
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  private auth = inject(Auth);

  registerData: RegisterRequest = {
    username: '',
    password: ''
  };

  register(): void {
    this.auth.register(this.registerData).subscribe({
      next: () => {
        console.log('Registration successful');
      },
      error: error => {
        console.error('Registration failed', error);
      }
    });
  }
}