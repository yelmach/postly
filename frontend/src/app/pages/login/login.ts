import { LoginRequest } from '@/models/auth';
import { AuthService } from '@/services/auth';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, inject, signal } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router, RouterLink } from '@angular/router';


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
    RouterLink
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
  formError = signal<string>('');
  fieldErrors = signal<{ [key: string]: string }>({});

  authService = inject(AuthService);
  router = inject(Router);


  onFormSubmit(e: Event) {
    e.preventDefault;

    this.formError.set('');
    this.fieldErrors.set({});

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
      error: (errorDetails: HttpErrorResponse) => {
        this.isLoading.set(false);
        this.handleError(errorDetails);
      }
    })
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error.details) {
      const details = error.error.details;

      this.fieldErrors.set(details);

      Object.keys(details).forEach(fieldName => {
        const control = this.loginForm.get(fieldName);
        if (control) {
          control.setErrors({ backend: details[fieldName] });
          control.markAsTouched();
        }
      });
      return;
    }

    if (error.error.message) {
      this.formError.set(error.error.message);
    } else if (error.error.error) {
      this.formError.set(error.error.error);
    } else {
      this.formError.set("An error occurred during login. status: " + error.status);
    }
  }

  getErrorMessage(fieldName: string): string {
    const backendError = this.fieldErrors()[fieldName];
    if (backendError) {
      return backendError.toString();
    }

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