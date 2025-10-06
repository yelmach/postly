import { Component, input, output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { User } from '@/models/user';

@Component({
  selector: 'app-user-card',
  imports: [CommonModule, MatButtonModule, MatIconModule],
  templateUrl: './user-card.component.html',
  styleUrl: './user-card.component.scss',
})
export class UserCardComponent {
  // Inputs
  user = input.required<User>();
  showSubscribeButton = input<boolean>(true);
  currentUserId = input<number | undefined>(undefined);

  // Outputs
  userClick = output<User>();
  subscribeToggle = output<User>();

  onUserClick() {
    this.userClick.emit(this.user());
  }

  onSubscribeClick(event: Event) {
    event.stopPropagation();
    this.subscribeToggle.emit(this.user());
  }

  shouldShowSubscribeButton(): boolean {
    const user = this.user();
    const currentId = this.currentUserId();

    if (!currentId) return false;
    if (user.id === currentId) return false;

    return true;
  }
}
