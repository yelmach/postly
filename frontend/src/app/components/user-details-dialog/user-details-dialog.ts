import { Component, inject, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';
import { AdminUser } from '@/models/admin';
import { getAdminDisplayDate } from '@/utils/date-utils';

export interface UserDetailsDialogData {
  user: AdminUser;
}

@Component({
  selector: 'app-user-details-dialog',
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatDividerModule,
  ],
  templateUrl: './user-details-dialog.html',
  styleUrl: './user-details-dialog.scss',
})
export class UserDetailsDialog {
  private dialogRef = inject(MatDialogRef<UserDetailsDialog>);

  constructor(@Inject(MAT_DIALOG_DATA) public data: UserDetailsDialogData) {}

  get fullName(): string {
    return `${this.data.user.firstName} ${this.data.user.lastName}`;
  }

  getAdminDisplayDate(date: Date | string): string {
    return getAdminDisplayDate(date);
  }

  onViewProfile() {
    window.open(`/profile/${this.data.user.username}`, '_blank');
  }

  onClose() {
    this.dialogRef.close();
  }
}
