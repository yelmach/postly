import { Component, inject, Inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { HttpErrorResponse } from '@angular/common/http';
import { ReportService } from '@/services/report.service';
import { getAllReportReasons, ReportReason, getReasonLabel } from '@/models/report';
import { ErrorBannerComponent } from '@/components/error-banner/error-banner.component';

export interface ReportDialogData {
  type: 'post' | 'user';
  targetId: number;
  targetTitle?: string; // For posts
  targetUsername?: string; // For users
}

@Component({
  selector: 'app-report-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatSelectModule,
    ErrorBannerComponent,
  ],
  templateUrl: './report-dialog.html',
  styleUrl: './report-dialog.scss',
})
export class ReportDialog {
  private reportService = inject(ReportService);
  private dialogRef = inject(MatDialogRef<ReportDialog>);

  reportForm = new FormGroup({
    reason: new FormControl<ReportReason | null>(null, [Validators.required]),
    description: new FormControl('', [Validators.maxLength(1000)]),
  });

  currentStep = signal<'select' | 'confirm'>('select');
  isLoading = signal(false);
  formError = signal('');
  reportReasons = getAllReportReasons();

  dialogTitle = computed(() => `Report ${this.data.type === 'post' ? 'Post' : 'User'}`);

  dialogDescription = computed(() =>
    this.data.type === 'post'
      ? "Help us understand what's wrong with this post. Your report will be reviewed by our moderation team."
      : "Help us understand what's wrong with this user's behavior. Your report will be reviewed by our moderation team."
  );

  targetLabel = computed(() => (this.data.type === 'post' ? 'Post' : 'User'));

  targetValue = computed(() =>
    this.data.type === 'post'
      ? this.data.targetTitle
      : this.data.targetUsername
      ? `@${this.data.targetUsername}`
      : ''
  );

  constructor(@Inject(MAT_DIALOG_DATA) public data: ReportDialogData) {}

  get selectedReason() {
    const reason = this.reportForm.get('reason')?.value;
    return reason ? getReasonLabel(reason) : '';
  }

  get description() {
    return this.reportForm.get('description')?.value || '';
  }

  get descriptionLength() {
    return this.description.length;
  }

  onNext() {
    if (this.reportForm.get('reason')?.invalid) {
      this.formError.set('Please select a reason for reporting');
      return;
    }

    this.formError.set('');
    this.currentStep.set('confirm');
  }

  onBack() {
    this.currentStep.set('select');
    this.formError.set('');
  }

  onSubmit() {
    if (this.reportForm.invalid) {
      this.formError.set('Please fill in all required fields');
      return;
    }

    this.isLoading.set(true);
    this.formError.set('');

    const request = {
      reason: this.reportForm.get('reason')?.value as ReportReason,
      description: this.reportForm.get('description')?.value || undefined,
    };

    const submitObservable =
      this.data.type === 'post'
        ? this.reportService.reportPost(this.data.targetId, request)
        : this.reportService.reportUser(this.data.targetId, request);

    submitObservable.subscribe({
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
    const entityType = this.data.type;

    if (error.status === 409) {
      this.formError.set(`You have already reported this ${entityType}`);
    } else if (error.status === 400) {
      if (error.error?.message?.includes('cannot report yourself')) {
        this.formError.set('You cannot report yourself');
      } else if (error.error?.message) {
        this.formError.set(error.error.message);
      } else {
        this.formError.set('Invalid request. Please try again.');
      }
    } else if (error.error?.message) {
      this.formError.set(error.error.message);
    } else {
      this.formError.set('Failed to submit report. Please try again.');
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
