import { Component, inject, Inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ModerationService } from '@/services/moderation.service';
import { ReportResponse, getReasonLabel } from '@/models/report';
import {
  ModerationAction,
  getModerationActionLabel,
  getAvailableActionsForReport,
} from '@/models/admin';
import { ErrorBannerComponent } from '@/components/error-banner/error-banner.component';
import { MatTooltip } from '@angular/material/tooltip';

export interface ReportDetailsDialogData {
  report: ReportResponse;
}

@Component({
  selector: 'app-report-details-dialog',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatIconModule,
    ErrorBannerComponent,
  ],
  templateUrl: './report-details-dialog.html',
  styleUrl: './report-details-dialog.scss',
})
export class ReportDetailsDialog {
  private moderationService = inject(ModerationService);
  private dialogRef = inject(MatDialogRef<ReportDetailsDialog>);
  private router = inject(Router);

  resolveForm = new FormGroup({
    action: new FormControl<ModerationAction>(ModerationAction.NO_ACTION, [Validators.required]),
    adminNotes: new FormControl('', [Validators.maxLength(1000)]),
  });

  isLoading = signal(false);
  formError = signal('');

  availableActions = computed(() => getAvailableActionsForReport(this.data.report));

  reportType = computed(() => (this.data.report.reportedUser ? 'User' : 'Post'));

  reportedEntityName = computed(() => {
    if (this.data.report.reportedUser) {
      return `@${this.data.report.reportedUser.username}`;
    } else if (this.data.report.reportedPost) {
      return this.data.report.reportedPost.title;
    }
    return 'Unknown';
  });

  isPending = computed(() => this.data.report.status === 'PENDING');

  constructor(@Inject(MAT_DIALOG_DATA) public data: ReportDetailsDialogData) {}

  get adminNotesLength() {
    return this.resolveForm.get('adminNotes')?.value?.length || 0;
  }

  getReasonLabel(reason: string): string {
    return getReasonLabel(reason as any);
  }

  getModerationActionLabel(action: ModerationAction): string {
    return getModerationActionLabel(action);
  }

  onViewReportedEntity() {
    if (this.data.report.reportedUser) {
      const url = this.router.serializeUrl(
        this.router.createUrlTree(['/profile', this.data.report.reportedUser.username])
      );
      window.open(url, '_blank');
    } else if (this.data.report.reportedPost) {
      window.open(`/post/${this.data.report.reportedPost.id}`, '_blank');
    }
  }

  onViewReporter() {
    const url = this.router.serializeUrl(
      this.router.createUrlTree(['/profile', this.data.report.reporter.username])
    );
    window.open(url, '_blank');
  }

  onDismiss() {
    if (!this.isPending()) {
      this.formError.set('This report has already been resolved');
      return;
    }

    this.isLoading.set(true);
    this.formError.set('');

    const request = {
      status: 'DISMISSED' as const,
      adminNotes: this.resolveForm.get('adminNotes')?.value || undefined,
    };

    this.moderationService.resolveReport(this.data.report.id, request).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.dialogRef.close({ success: true, action: 'dismissed' });
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        this.handleError(error);
      },
    });
  }

  onResolve() {
    if (!this.isPending()) {
      this.formError.set('This report has already been resolved');
      return;
    }

    if (this.resolveForm.invalid) {
      this.formError.set('Please fill in all required fields');
      return;
    }

    this.isLoading.set(true);
    this.formError.set('');

    const action = this.resolveForm.get('action')?.value || ModerationAction.NO_ACTION;

    const request = {
      status: 'RESOLVED' as const,
      adminNotes: this.resolveForm.get('adminNotes')?.value || undefined,
      action: action !== ModerationAction.NO_ACTION ? action : undefined,
    };

    this.moderationService.resolveReport(this.data.report.id, request).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.dialogRef.close({ success: true, action: 'resolved', moderationAction: action });
      },
      error: (error: HttpErrorResponse) => {
        this.isLoading.set(false);
        this.handleError(error);
      },
    });
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 400) {
      if (error.error?.message) {
        this.formError.set(error.error.message);
      } else {
        this.formError.set('Invalid request. Please try again.');
      }
    } else if (error.status === 403) {
      this.formError.set('You do not have permission to resolve reports');
    } else if (error.status === 404) {
      this.formError.set('Report not found');
    } else if (error.error?.message) {
      this.formError.set(error.error.message);
    } else {
      this.formError.set('Failed to resolve report. Please try again.');
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
