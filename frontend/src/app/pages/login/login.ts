import { ApiError, LoginRequest } from '@/models/auth';
import { AuthService } from '@/services/auth';
import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router } from '@angular/router';


@Component({
  selector: 'login-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatLabel,
    MatProgressSpinnerModule,
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  loginForm = new FormGroup({
    credentials: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
  }

  )
  hidePassword = signal(false);
  isLoading = signal(false);

  authService = inject(AuthService);
  router = inject(Router);


  onFormSubmit(e: Event) {
    e.preventDefault;

    if (this.loginForm.invalid) {
      return;
    }

    this.isLoading.set(true);

    const credentials = this.loginForm.get('credentials')?.value ?? '';
    const password = this.loginForm.get('password')?.value ?? '';
    const formData: LoginRequest = { credentials, password };

    this.authService.login(formData).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/home']);
      },
      error: (errorDetails: ApiError) => {
        this.isLoading.set(false);
      }
    })
  }

  getErrorMessage(fieldName: string): string {
    const field = this.loginForm.get(fieldName);

    if (!field || field.valid || field.untouched) {
      return '';
    }

    const errors = field.errors;
    if (!errors) {
      return '';
    }

    if (errors['required']) {
      return `${fieldName} is required`;
    }

    return '';
  }

  togglePasswordVisibility() {
    this.hidePassword.set(!this.hidePassword());
  }
}