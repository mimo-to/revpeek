package com.revpeek.output;

import com.revpeek.analytics.AnalyticsService;
import com.revpeek.git.model.Commit;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class TerminalRenderer {
    
    private boolean noColor;
    
    public TerminalRenderer(boolean noColor) {
        this.noColor = noColor ||
            Boolean.getBoolean("revpeek.no-color") ||
            System.getenv("NO_COLOR") != null ||
            System.console() == null;
    }
    
    public void render(AnalyticsService.CommitStats stats) {
        System.out.println(getColored("+-----------------------------------------+", "\033[36m")); // Cyan
        System.out.println(getColored("|              \033[1mRevPeek Analytics\033[0m              |", "\033[36m")); // Cyan, bold
        System.out.println(getColored("+-----------------------------------------+", "\033[36m")); // Cyan
        System.out.println();
        
        // Summary
        System.out.println(getColored("Summary", "\033[1;33m")); // Bold yellow
        System.out.println("  Total Commits: " + getColored(String.valueOf(stats.totalCommits), "\033[32m")); // Green
        System.out.println(" Date Range: " + getColored(stats.firstCommitDate + " to " + stats.lastCommitDate, "\033[32m")); // Green
        System.out.println();
        
        // Top Authors
        System.out.println(getColored("Top Authors", "\033[1;33m")); // Bold yellow
        renderTopItems(stats.authorCommits, 10);
        System.out.println();
        
        // Commit Activity
        System.out.println(getColored("Commit Activity", "\033[1;33m")); // Bold yellow
        renderBarChart(stats.dailyCommits, 20);
        System.out.println();
        
        // File Types
        System.out.println(getColored("File Types", "\033[1;33m")); // Bold yellow
        renderTopItems(stats.fileTypeDistribution, 10);
        System.out.println();
        
        System.out.println(getColored("Run with --help for more options", "\033[2m")); // Dim
    }
    
    private void renderTopItems(Map<String, Integer> items, int maxItems) {
        // Sort by value in descending order and limit to maxItems
        Map<String, Integer> sortedItems = items.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(maxItems)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        
        int maxCount = sortedItems.values().stream().mapToInt(Integer::intValue).max().orElse(1);
        
        for (Map.Entry<String, Integer> entry : sortedItems.entrySet()) {
            String key = entry.getKey();
            int count = entry.getValue();
            
            // Create a simple bar using Unicode blocks
            int barLength = Math.max(1, (count * 30) / maxCount);
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < barLength; i++) {
                bar.append("█");
            }
            
            System.out.printf("  %-25s %5d %s\n", key, count, getColored(bar.toString(), "\033[36m")); // Cyan
        }
        
        if (items.size() > maxItems) {
            System.out.println("  ... and " + (items.size() - maxItems) + " more");
        }
    }
    
    private void renderBarChart(Map<String, Integer> data, int maxBars) {
        if (data.isEmpty()) {
            System.out.println(" No data to display");
            return;
        }
        
        // Sort by date to show chronological order
        Map<String, Integer> sortedData = data.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .limit(maxBars)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (e1, e2) -> e1,
                LinkedHashMap::new
            ));
        
        int maxCount = sortedData.values().stream().mapToInt(Integer::intValue).max().orElse(1);
        
        for (Map.Entry<String, Integer> entry : sortedData.entrySet()) {
            String date = entry.getKey();
            int count = entry.getValue();
            
            // Create a simple bar using Unicode blocks
            int barLength = Math.max(1, (count * 30) / maxCount);
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < barLength; i++) {
                bar.append("█");
            }
            
            System.out.printf("  %-12s %3d %s\n", date, count, getColored(bar.toString(), "\033[35m")); // Magenta
        }
    }
    
    private String getColored(String text, String colorCode) {
        if (this.noColor) {
            return text;
        }
        return colorCode + text + "\033[0m";
    }
}