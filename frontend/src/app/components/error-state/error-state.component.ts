import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-error-state',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule],
  templateUrl: './error-state.component.html',
  styleUrl: './error-state.component.scss',
})
export class ErrorStateComponent {
  // Inputs
  title = input.required<string>();
  message = input<string>('');
  icon = input<string>('error_outline');
  actionLabel = input<string>('');

  // Outputs
  actionClick = output<void>();

  onActionClick() {
    this.actionClick.emit();
  }
}
