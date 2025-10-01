import { ApiError, RegisterRequest } from "@/models/auth";
import { AuthService } from "@/services/auth";
import { CommonModule } from "@angular/common";
import { Component, inject, signal } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatFormFieldModule, MatLabel } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { Router } from "@angular/router";

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
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  registerForm: FormGroup = new FormGroup({
    firstName: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(50),
      Validators.pattern(/^[a-zA-Z\s]+$/)
    ]),
    lastName: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(50),
      Validators.pattern(/^[a-zA-Z\s]+$/)
    ]),
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(3),
      Validators.maxLength(50),
      Validators.pattern(/^[a-zA-Z0-9_]+$/)
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.email,
      Validators.maxLength(100)
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      Validators.maxLength(100)
    ]),
    bio: new FormControl('', Validators.maxLength(512)),
  })

  hidePassword = signal(true);
  isLoading = signal(false);
  errorField = signal('');
  authService = inject(AuthService);
  router = inject(Router);


  onFormSubmit() {
    if (this.registerForm.invalid) {
      return;
    }

    this.isLoading.set(true);

    const formData: RegisterRequest = this.registerForm.value;
    console.log(formData)

    this.authService.register(formData).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate(['/home']);
      },
      error: (errorDetails: ApiError) => {
        this.isLoading.set(false);
        this.errorField.set(errorDetails.message);
      }
    })
  }

  getErrorMessage(fieldName: string): string {
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