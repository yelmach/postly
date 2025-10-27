import { Injectable, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable, map } from 'rxjs';
import {
  ConfirmDialogComponent,
  ConfirmDialogData,
} from '@/components/confirm-dialog/confirm-dialog.component';

@Injectable({
  providedIn: 'root',
})
export class ConfirmDialogService {
  private dialog = inject(MatDialog);

  confirm(data: ConfirmDialogData): Observable<boolean> {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data,
      width: '400px',
      maxWidth: '90vw',
    });

    return dialogRef.afterClosed().pipe(map((result) => result === true));
  }

  confirmDelete(itemName: string): Observable<boolean> {
    return this.confirm({
      title: `Delete ${itemName}?`,
      message: `Are you sure you want to delete this ${itemName}? This action cannot be undone.`,
      confirmText: 'Delete',
      cancelText: 'Cancel',
    });
  }

  confirmDiscard(itemName: string = 'changes'): Observable<boolean> {
    return this.confirm({
      title: `Discard ${itemName}?`,
      message: `Are you sure you want to discard your ${itemName}? This action cannot be undone.`,
      confirmText: 'Discard',
      cancelText: 'Keep Editing',
    });
  }

  confirmAdminAction(action: string, username: string): Observable<boolean> {
    return this.confirm({
      title: `${action} user?`,
      message: `Are you sure you want to ${action.toLowerCase()} "${username}"?`,
      confirmText: 'Confirm',
      cancelText: 'Cancel',
    });
  }
}
