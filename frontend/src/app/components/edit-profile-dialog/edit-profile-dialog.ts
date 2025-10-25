import { Component, inject, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { UserService } from '@/services/user.service';
import { AuthService } from '@/services/auth.service';
import { User } from '@/models/user';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorBannerComponent } from '@/components/error-banner/error-banner.component';

@Component({
  selector: 'app-edit-profile-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatProgressSpinnerModule,
    ErrorBannerComponent,
  ],
  templateUrl: './edit-profile-dialog.html',
  styleUrl: './edit-profile-dialog.scss',
})
export class EditProfileDialog {
  private userService = inject(UserService);
  private authService = inject(AuthService);
  private dialogRef = inject(MatDialogRef<EditProfileDialog>);

  editForm = new FormGroup({
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
    email: new FormControl('', [Validators.required, Validators.email, Validators.maxLength(100)]),
    password: new FormControl('', [Validators.minLength(6), Validators.maxLength(100)]),
    bio: new FormControl('', [Validators.maxLength(255)]),
  });

  hidePassword = signal(true);
  isLoading = signal(false);
  formError = signal('');
  selectedFile = signal<File | null>(null);
  previewUrl = signal<string | null>(null);

  constructor(@Inject(MAT_DIALOG_DATA) public data: { user: User }) {
    this.editForm.patchValue({
      firstName: data.user.firstName,
      lastName: data.user.lastName,
      email: data.user.email,
      bio: data.user.bio || '',
    });
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      const file = input.files[0];

      if (file.size > 5 * 1024 * 1024) {
        this.formError.set('File size must not exceed 5MB');
        return;
      }

      const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/webp'];
      if (!allowedTypes.includes(file.type)) {
        this.formError.set('Only JPG, PNG, and WebP images are allowed');
        return;
      }

      this.selectedFile.set(file);
      this.formError.set('');

      const reader = new FileReader();
      reader.onload = (e) => {
        this.previewUrl.set(e.target?.result as string);
      };
      reader.readAsDataURL(file);
    }
  }

  clearSelectedFile() {
    this.selectedFile.set(null);
    this.previewUrl.set(null);
  }

  getFileSize(): string {
    const file = this.selectedFile();
    if (!file) return '';
    const sizeInKB = file.size / 1024;
    if (sizeInKB < 1024) {
      return `${sizeInKB.toFixed(2)} KB`;
    }
    return `${(sizeInKB / 1024).toFixed(2)} MB`;
  }

  async onSave() {
    this.formError.set('');
    this.isLoading.set(true);

    try {
      if (this.selectedFile()) {
        await this.uploadProfilePicture();
      }

      if (this.editForm.dirty) {
        await this.updateProfileData();
      }

      this.isLoading.set(false);
      this.dialogRef.close({ success: true });
    } catch (error) {
      this.isLoading.set(false);
      this.handleError(error as HttpErrorResponse);
    }
  }

  private uploadProfilePicture(): Promise<void> {
    return new Promise((resolve, reject) => {
      const file = this.selectedFile();
      if (!file) {
        resolve();
        return;
      }

      const formData = new FormData();
      formData.append('file', file);

      this.userService.updateProfilePicture(formData).subscribe({
        next: (updatedUser) => {
          this.authService.currentUser.set(updatedUser);
          resolve();
        },
        error: (error) => reject(error),
      });
    });
  }

  private updateProfileData(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (this.editForm.invalid) {
        reject(new Error('Form is invalid'));
        return;
      }

      const formData = this.editForm.value;

      const updateData: any = {};
      if (formData.firstName && formData.firstName !== this.data.user.firstName) {
        updateData.firstName = formData.firstName;
      }
      if (formData.lastName && formData.lastName !== this.data.user.lastName) {
        updateData.lastName = formData.lastName;
      }
      if (formData.email && formData.email !== this.data.user.email) {
        updateData.email = formData.email;
      }
      if (formData.password && formData.password.trim() !== '') {
        updateData.password = formData.password;
      }
      if (formData.bio !== this.data.user.bio) {
        updateData.bio = formData.bio;
      }

      if (Object.keys(updateData).length === 0) {
        resolve();
        return;
      }

      this.userService.updateProfile(updateData).subscribe({
        next: (updatedUser) => {
          this.authService.currentUser.set(updatedUser);
          resolve();
        },
        error: (error) => reject(error),
      });
    });
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error?.message) {
      this.formError.set(error.error.message);
    } else if (error.error?.error) {
      this.formError.set(error.error.error);
    } else {
      this.formError.set('Failed to update profile. Please try again.');
    }
  }

  getErrorMessage(fieldName: string): string {
    const field = this.editForm.get(fieldName);
    if (!field || field.valid || field.untouched) return '';

    const errors = field.errors;
    if (!errors) return '';

    if (errors['required']) return `${fieldName} is required`;
    if (errors['minlength'])
      return `Must be at least ${errors['minlength'].requiredLength} characters`;
    if (errors['maxlength'])
      return `Must not exceed ${errors['maxlength'].requiredLength} characters`;
    if (errors['email']) return 'Enter a valid email';
    if (errors['pattern']) {
      if (fieldName === 'firstName' || fieldName === 'lastName') {
        return 'Only letters and spaces allowed';
      }
    }

    return '';
  }

  togglePasswordVisibility() {
    this.hidePassword.update((v) => !v);
  }

  onCancel() {
    this.dialogRef.close();
  }
}
