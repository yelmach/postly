import { RegisterRequest } from '@/models/auth';
import { AuthService } from '@/services/auth.service';
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
import { ErrorBannerComponent } from '@/components/error-banner/error-banner.component';

@Component({
  selector: 'register-page',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatLabel,
    MatProgressSpinnerModule,
    RouterLink,
    ErrorBannerComponent,
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  registerForm: FormGroup = new FormGroup({
    firstName: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(50),
      Validators.pattern(/^[a-zA-Z\s]+$/),
    ]),
    lastName: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(50),
      Validators.pattern(/^[a-zA-Z\s]+$/),
    ]),
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(50),
      Validators.pattern(/^[a-zA-Z0-9_]+$/),
    ]),
    email: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(100)]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      Validators.maxLength(100),
    ]),
    bio: new FormControl('', Validators.maxLength(255)),
  });

  hidePassword = signal(true);
  isLoading = signal(false);
  formError = signal<string>('');
  fieldErrors = signal<{ [key: string]: string }>({});

  authService = inject(AuthService);
  router = inject(Router);

  onFormSubmit(e: Event) {
    e.preventDefault;

    this.formError.set('');
    this.fieldErrors.set({});

    Object.keys(this.registerForm.controls).forEach((key) => {
      const control = this.registerForm.get(key);
      if (control && typeof control.value === 'string' && key !== 'password') {
        control.setValue(control.value.trim());
      }
    });

    if (this.registerForm.invalid) {
      return;
    }

    this.isLoading.set(true);

    const formData: RegisterRequest = this.registerForm.value;

    this.authService.register(formData).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/home']);
      },
      error: (errorDetails: HttpErrorResponse) => {
        this.isLoading.set(false);
        this.handleError(errorDetails);
      },
    });
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error?.details) {
      const details = error.error.details;
      this.fieldErrors.set(details);

      Object.keys(details).forEach((fieldName) => {
        const control = this.registerForm.get(fieldName);
        if (control) {
          control.setErrors({ backend: details[fieldName] });
          control.markAsTouched();
        }
      });
      return;
    }

    if (error.error?.message) {
      const message = error.error.message;

      this.formError.set(message);

      return;
    }

    if (error.error?.error) {
      this.formError.set(error.error.error);
    } else {
      this.formError.set('Registration failed. status: ' + error.status);
    }
  }

  getErrorMessage(fieldName: string): string {
    const backendError = this.fieldErrors()[fieldName];
    if (backendError) {
      return backendError;
    }

    const field = this.registerForm.get(fieldName);

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

    if (errors['minlength']) {
      const minLength = errors['minlength'].requiredLength;
      return `must be at least ${minLength} characters`;
    }

    if (errors['maxlength']) {
      const maxLength = errors['maxlength'].requiredLength;
      return `must not exceed ${maxLength} characters`;
    }

    if (errors['email']) {
      return 'enter a valid email';
    }

    if (errors['pattern']) {
      if (fieldName == 'firstName' || fieldName == 'lastName') {
        return `only contain letters and spaces`;
      }
      if (fieldName === 'username') {
        return 'only contain letters, numbers, and underscores';
      }
    }

    return '';
  }

  togglePasswordVisibility() {
    this.hidePassword.set(!this.hidePassword());
  }
}
