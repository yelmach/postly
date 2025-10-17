package postly.dto.response;

public record DashboardStatsResponse(UserStats userStats, PostStats postStats, ReportStats reportStats) {

        public record UserStats(long total, long newLastMonth) {
        }

        public record PostStats(long total, long newLastMonth) {
        }

        public record ReportStats(long total, long activePending) {
        }
}
