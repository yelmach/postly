import { Component, input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-loading-spinner',
  imports: [CommonModule, MatProgressSpinnerModule],
  templateUrl: './loading-spinner.component.html',
  styleUrl: './loading-spinner.component.scss',
})
export class LoadingSpinnerComponent {
  size = input<'small' | 'medium' | 'large'>('medium');
  message = input<string>('');
  fullScreen = input<boolean>(false);

  getDiameter(): number {
    const sizes = {
      small: 30,
      medium: 50,
      large: 70,
    };
    return sizes[this.size()];
  }
}
