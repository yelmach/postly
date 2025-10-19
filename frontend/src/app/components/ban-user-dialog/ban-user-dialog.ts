import { Component, inject, Inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { HttpErrorResponse } from '@angular/common/http';
import { ModerationService } from '@/services/moderation.service';
import { ErrorBannerComponent } from '@/components/error-banner/error-banner.component';

export interface BanUserDialogData {
  userId: number;
  username: string;
}

@Component({
  selector: 'app-ban-user-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    ErrorBannerComponent,
  ],
  templateUrl: './ban-user-dialog.html',
  styleUrl: './ban-user-dialog.scss',
})
export class BanUserDialog {
  private moderationService = inject(ModerationService);
  private dialogRef = inject(MatDialogRef<BanUserDialog>);

  banForm = new FormGroup({
    reason: new FormControl('', [Validators.required, Validators.maxLength(1000)]),
    permanent: new FormControl(false),
    durationDays: new FormControl<number | null>(null, [Validators.min(1)]),
  });

  isLoading = signal(false);
  formError = signal('');

  constructor(@Inject(MAT_DIALOG_DATA) public data: BanUserDialogData) {}

  get reasonLength() {
    return this.banForm.get('reason')?.value?.length || 0;
  }

  get isPermanent() {
    return this.banForm.get('permanent')?.value || false;
  }

  onPermanentChange() {
    const isPermanent = this.banForm.get('permanent')?.value;
    const durationControl = this.banForm.get('durationDays');

    if (isPermanent) {
      durationControl?.setValue(null);
      durationControl?.disable();
    } else {
      durationControl?.enable();
    }
  }

  onSubmit() {
    if (this.banForm.invalid) {
      this.formError.set('Please fill in all required fields');
      return;
    }

    const isPermanent = this.banForm.get('permanent')?.value || false;
    const durationDays = this.banForm.get('durationDays')?.value;

    if (!isPermanent && !durationDays) {
      this.formError.set('Please specify ban duration or select permanent ban');
      return;
    }

    this.isLoading.set(true);
    this.formError.set('');

    const request = {
      reason: this.banForm.get('reason')?.value || '',
      permanent: isPermanent,
      durationDays: isPermanent ? 0 : durationDays || undefined,
    };

    this.moderationService.banUser(this.data.userId, request).subscribe({
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
    if (error.status === 401) {
      this.formError.set('You do not have permission to ban users');
    } else if (error.error?.message) {
      this.formError.set(error.error.message);
    } else {
      this.formError.set('Failed to ban user. Please try again.');
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
