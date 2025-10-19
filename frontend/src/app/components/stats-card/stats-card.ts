import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-stats-card',
  imports: [CommonModule, MatCardModule, MatIconModule],
  templateUrl: './stats-card.html',
  styleUrl: './stats-card.scss',
})
export class StatsCard {
  @Input() icon: string = 'bar_chart';
  @Input() label: string = '';
  @Input() value: number = 0;
  @Input() sublabel: string = '';
  @Input() subvalue: number = 0;
  @Input() color: 'primary' | 'secondary' | 'tertiary' = 'primary';
}
