import { Component, inject, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { HttpErrorResponse } from '@angular/common/http';
import { ModerationService } from '@/services/moderation.service';
import { ErrorBannerComponent } from '@/components/error-banner/error-banner.component';

export interface HidePostDialogData {
  postId: number;
  postTitle: string;
}

@Component({
  selector: 'app-hide-post-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    ErrorBannerComponent,
  ],
  templateUrl: './hide-post-dialog.html',
  styleUrl: './hide-post-dialog.scss',
})
export class HidePostDialog {
  private moderationService = inject(ModerationService);
  private dialogRef = inject(MatDialogRef<HidePostDialog>);

  hideForm = new FormGroup({
    reason: new FormControl('', [Validators.required, Validators.maxLength(1000)]),
  });

  isLoading = signal(false);
  formError = signal('');

  constructor(@Inject(MAT_DIALOG_DATA) public data: HidePostDialogData) {}

  get reasonLength() {
    return this.hideForm.get('reason')?.value?.length || 0;
  }

  onSubmit() {
    if (this.hideForm.invalid) {
      this.formError.set('Please provide a reason for hiding this post');
      return;
    }

    this.isLoading.set(true);
    this.formError.set('');

    const request = {
      reason: this.hideForm.get('reason')?.value || '',
    };

    this.moderationService.hidePost(this.data.postId, request).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.dialogRef.close({ success: true });
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        this.handleError(error);
      },
    });
  }

  private handleError(error: HttpErrorResponse) {
    if (error.error?.message) {
      this.formError.set(error.error.message);
    } else {
      this.formError.set('Failed to hide post. Please try again.');
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
