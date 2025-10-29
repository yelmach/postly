export function getRelativeTime(date: Date | string): string {
  const now = new Date();
  const past = new Date(date);
  const diffInMs = now.getTime() - past.getTime();
  const diffInSeconds = Math.floor(diffInMs / 1000);
  const diffInMinutes = Math.floor(diffInSeconds / 60);
  const diffInHours = Math.floor(diffInMinutes / 60);
  const diffInDays = Math.floor(diffInHours / 24);
  const diffInWeeks = Math.floor(diffInDays / 7);
  const diffInMonths = Math.floor(diffInDays / 30);
  const diffInYears = Math.floor(diffInDays / 365);

  if (diffInSeconds < 10) return 'just now';
  if (diffInSeconds < 60) return 'a few seconds ago';
  if (diffInMinutes === 1) return '1 minute ago';
  if (diffInMinutes < 60) return `${diffInMinutes} minutes ago`;
  if (diffInHours === 1) return '1 hour ago';
  if (diffInHours < 24) return `${diffInHours} hours ago`;
  if (diffInDays === 1) return 'yesterday';
  if (diffInDays < 7) return `${diffInDays} days ago`;
  if (diffInWeeks === 1) return '1 week ago';
  if (diffInWeeks < 4) return `${diffInWeeks} weeks ago`;
  if (diffInMonths === 1) return '1 month ago';
  if (diffInMonths < 12) return `${diffInMonths} months ago`;
  if (diffInYears === 1) return '1 year ago';
  return `${diffInYears} years ago`;
}

export function getAdminDisplayDate(date: Date | string): string {
  const dateObj = new Date(date);
  const now = new Date();
  const diffInMs = now.getTime() - dateObj.getTime();
  const diffInDays = Math.floor(diffInMs / (1000 * 60 * 60 * 24));

  if (diffInDays < 7 && diffInMs > 0) {
    return getRelativeTime(date);
  }

  const options: Intl.DateTimeFormatOptions = {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  };

  return dateObj.toLocaleDateString('en-US', options);
}
