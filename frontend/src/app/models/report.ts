import { PostResponse } from './post';
import { User } from './user';

export enum ReportReason {
  SPAM = 'SPAM',
  HARASSMENT = 'HARASSMENT',
  INAPPROPRIATE_CONTENT = 'INAPPROPRIATE_CONTENT',
  HATE_SPEECH = 'HATE_SPEECH',
  MISINFORMATION = 'MISINFORMATION',
  OTHER = 'OTHER',
}

export enum ReportStatus {
  PENDING = 'PENDING',
  RESOLVED = 'RESOLVED',
  DISMISSED = 'DISMISSED',
}

export interface CreateReportRequest {
  reason: ReportReason;
  description?: string;
}

export interface ReportResponse {
  id: number;
  reporter: User;
  reportedUser?: User;
  reportedPost?: PostResponse;
  reason: ReportReason;
  description?: string;
  status: ReportStatus;
  reviewedBy?: User;
  adminNotes?: string;
  createdAt: string;
  reviewedAt?: string;
}

export function getReasonLabel(reason: ReportReason): string {
  const labels: Record<ReportReason, string> = {
    [ReportReason.SPAM]: 'Spam',
    [ReportReason.HARASSMENT]: 'Harassment',
    [ReportReason.INAPPROPRIATE_CONTENT]: 'Inappropriate Content',
    [ReportReason.HATE_SPEECH]: 'Hate Speech',
    [ReportReason.MISINFORMATION]: 'Misinformation',
    [ReportReason.OTHER]: 'Other',
  };
  return labels[reason];
}

export function getAllReportReasons(): { value: ReportReason; label: string }[] {
  return Object.values(ReportReason).map((reason) => ({
    value: reason,
    label: getReasonLabel(reason),
  }));
}
