import { Component, input, computed, signal, inject, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { PostResponse } from '@/models/post';
import { Router } from '@angular/router';
import { PostService } from '@/services/post.service';

@Component({
  selector: 'app-post-card',
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule, MatMenuModule],
  templateUrl: './post-card.html',
  styleUrl: './post-card.scss',
})
export class PostCard {
  private postService = inject(PostService);

  post = input.required<PostResponse>();
  currentUserId = input<number | undefined>(undefined);
  maxContentLength = input<number>(200);

  likesCount = signal<number>(0);
  isLiked = signal<boolean>(false);

  constructor(private router: Router) {
    effect(() => {
      const post = this.post();
      this.likesCount.set(post.likesCount);
      this.isLiked.set(post.isLikedByCurrentUser);
    });
  }

  plainTextContent = computed(() => {
    const content = this.post().content;

    // Remove images: ![alt](url)
    let plainText = content.replace(/!\[.*?\]\(.*?\)/g, '');

    // Remove links but keep text: [text](url) -> text
    plainText = plainText.replace(/\[(.*?)\]\(.*?\)/g, '$1');

    // Remove headers markers
    plainText = plainText.replace(/^#+\s+/gm, '');

    // Remove bold/italic markers
    plainText = plainText.replace(/\*\*\*(.+?)\*\*\*/g, '$1');
    plainText = plainText.replace(/\*\*(.+?)\*\*/g, '$1');
    plainText = plainText.replace(/\*(.+?)\*/g, '$1');
    plainText = plainText.replace(/___(.+?)___/g, '$1');
    plainText = plainText.replace(/__(.+?)__/g, '$1');
    plainText = plainText.replace(/_(.+?)_/g, '$1');

    // Remove code blocks
    plainText = plainText.replace(/```[\s\S]*?```/g, '');
    plainText = plainText.replace(/`(.+?)`/g, '$1');

    // Remove blockquotes
    plainText = plainText.replace(/^>\s+/gm, '');

    // Remove horizontal rules
    plainText = plainText.replace(/^---$/gm, '');
    plainText = plainText.replace(/^\*\*\*$/gm, '');

    // Remove list markers
    plainText = plainText.replace(/^[\*\-\+]\s+/gm, '');
    plainText = plainText.replace(/^\d+\.\s+/gm, '');

    // Clean up extra whitespace and newlines
    plainText = plainText.replace(/\n\n+/g, ' ');
    plainText = plainText.replace(/\n/g, ' ');
    plainText = plainText.trim();

    // Truncate if needed
    const maxLength = this.maxContentLength();
    if (plainText.length > maxLength) {
      return plainText.substring(0, maxLength) + '...';
    }

    return plainText;
  });

  onPostClick() {
    this.router.navigate(['/post', this.post().id]);
  }

  onAuthorClick(event: Event) {
    event.stopPropagation();
    const author = this.post().author;
    this.router.navigate(['/profile', author.username]);
  }

  onLikeClick(event: Event) {
    event.stopPropagation();

    const wasLiked = this.isLiked();
    this.isLiked.set(!wasLiked);
    this.likesCount.update((count) => (wasLiked ? count - 1 : count + 1));

    this.postService.toggleLike(this.post().id).subscribe({
      next: (response) => {
        this.isLiked.set(response.liked);
      },
      error: (error) => {
        console.error('Failed to toggle like:', error);
        this.isLiked.set(wasLiked);
        this.likesCount.update((count) => (wasLiked ? count + 1 : count - 1));
      },
    });
  }

  onEditClick(event: Event) {
    event.stopPropagation();
    // TODO
  }

  onDeleteClick(event: Event) {
    event.stopPropagation();
    // TODO
  }

  onReportClick(event: Event) {
    event.stopPropagation();
    // TODO
  }

  onMediaClick(event: Event, mediaUrl: string) {
    event.stopPropagation();
    window.open(mediaUrl, '_blank');
  }

  getTruncatedContent(): string {
    const content = this.post().content;
    const maxLength = this.maxContentLength();

    if (content.length <= maxLength) {
      return content;
    }

    return content.substring(0, maxLength) + '...';
  }

  isPostOwner(): boolean {
    const currentId = this.currentUserId();
    return currentId !== undefined && currentId === this.post().author.id;
  }

  getRelativeTime(dateString: string): string {
    const date = new Date(dateString);
    const now = new Date();
    const seconds = Math.floor((now.getTime() - date.getTime()) / 1000);

    if (seconds < 60) return 'just now';
    if (seconds < 3600) return `${Math.floor(seconds / 60)}m ago`;
    if (seconds < 86400) return `${Math.floor(seconds / 3600)}h ago`;
    if (seconds < 604800) return `${Math.floor(seconds / 86400)}d ago`;
    if (seconds < 2592000) return `${Math.floor(seconds / 604800)}w ago`;

    return date.toLocaleDateString();
  }
}
