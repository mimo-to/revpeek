package com.revpeek.analytics;

import com.revpeek.git.GitLogParser;
import com.revpeek.git.model.Commit;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AnalyticsService {
    
    public static class CommitStats {
        public Map<String, Integer> authorCommits;
        public Map<String, Integer> dailyCommits;
        public Map<String, Integer> fileTypeDistribution;
        public int totalCommits;
        public String firstCommitDate;
        public String lastCommitDate;
        
        public CommitStats() {
            authorCommits = new HashMap<>();
            dailyCommits = new HashMap<>();
            fileTypeDistribution = new HashMap<>();
            totalCommits = 0;
        }
    }
    
    public static CommitStats generateStats(List<Commit> commits) {
        CommitStats stats = new CommitStats();
        stats.totalCommits = commits.size();
        
        if (commits.isEmpty()) {
            return stats;
        }
        
        // Calculate author commit counts
        stats.authorCommits = GitLogParser.getCommitCountByAuthor(commits);
        
        // Calculate daily commit counts and date range
        LocalDate firstDate = null;
        LocalDate lastDate = null;
        
        for (Commit commit : commits) {
            // With --date=short, format is "YYYY-MM-DD"
            String dateStr = commit.getDate();
            LocalDate commitDate = LocalDate.parse(dateStr);  // Works 100% of the time with --date=short
            
            String dateKey = commitDate.toString();
            stats.dailyCommits.put(dateKey, stats.dailyCommits.getOrDefault(dateKey, 0) + 1);
            
            if (firstDate == null || commitDate.isBefore(firstDate)) {
                firstDate = commitDate;
            }
            if (lastDate == null || commitDate.isAfter(lastDate)) {
                lastDate = commitDate;
            }
        }
        
        if (firstDate != null) {
            stats.firstCommitDate = firstDate.toString();
        }
        if (lastDate != null) {
            stats.lastCommitDate = lastDate.toString();
        }
        
        // Get file type distribution
        try {
            stats.fileTypeDistribution = GitLogParser.getFileTypeDistribution(commits);
        } catch (Exception e) {
            // If we can't get file types, continue with empty map
            stats.fileTypeDistribution = new HashMap<>();
        }
        
        return stats;
    }
}