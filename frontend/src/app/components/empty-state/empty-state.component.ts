import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule],
  templateUrl: './empty-state.component.html',
  styleUrl: './empty-state.component.scss',
})
export class EmptyStateComponent {
  // Inputs
  icon = input.required<string>();
  message = input.required<string>();
  actionLabel = input<string>('');

  // Outputs
  actionClick = output<void>();

  onActionClick() {
    this.actionClick.emit();
  }
}
