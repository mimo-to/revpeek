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
        System.out.print(renderToString(stats));
    }
    
    public String renderToString(AnalyticsService.CommitStats stats) {
        StringBuilder sb = new StringBuilder();
        
        sb.append(getColored("+-----------------------------------------+\n", "\033[36m")); // Cyan
        sb.append(getColored("|              \033[1mRevPeek Analytics\033[0m              |\n", "\033[36m")); // Cyan, bold
        sb.append(getColored("+-----------------------------------------+\n", "\033[36m")); // Cyan
        sb.append("\n");
        
        // Summary
        sb.append(getColored("Summary\n", "\033[1;33m")); // Bold yellow
        sb.append("  Total Commits: ").append(getColored(String.valueOf(stats.totalCommits), "\033[32m")).append("\n"); // Green
        sb.append(" Date Range: ").append(getColored(stats.firstCommitDate + " to " + stats.lastCommitDate, "\033[32m")).append("\n"); // Green
        sb.append("\n");
        
        // Top Authors
        sb.append(getColored("Top Authors\n", "\033[1;33m")); // Bold yellow
        renderTopItems(sb, stats.authorCommits, 10);
        sb.append("\n");
        
        // Commit Activity
        sb.append(getColored("Commit Activity\n", "\033[1;33m")); // Bold yellow
        renderBarChart(sb, stats.dailyCommits, 20);
        sb.append("\n");
        
        // File Types
        sb.append(getColored("File Types\n", "\033[1;33m")); // Bold yellow
        renderTopItems(sb, stats.fileTypeDistribution, 10);
        sb.append("\n");
        
        sb.append(getColored("Run with --help for more options\n", "\033[2m")); // Dim
        
        return sb.toString();
    }
    
    private void renderTopItems(StringBuilder sb, Map<String, Integer> items, int maxItems) {
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
            
            // Create a simple bar using ASCII characters
            int barLength = Math.max(1, (count * 30) / maxCount);
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < barLength; i++) {
                bar.append("#");
            }
            
            sb.append(String.format("  %-25s %5d %s\n", key, count, getColored(bar.toString(), "\033[36m"))); // Cyan
        }
        
        if (items.size() > maxItems) {
            sb.append("  ... and ").append(items.size() - maxItems).append(" more\n");
        }
    }
    
    private void renderBarChart(StringBuilder sb, Map<String, Integer> data, int maxBars) {
        if (data.isEmpty()) {
            sb.append(" No data to display\n");
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
            
            // Create a simple bar using ASCII characters
            int barLength = Math.max(1, (count * 30) / maxCount);
            StringBuilder bar = new StringBuilder();
            for (int i = 0; i < barLength; i++) {
                bar.append("#");
            }
            
            sb.append(String.format("  %-12s %3d %s\n", date, count, getColored(bar.toString(), "\033[35m"))); // Magenta
        }
    }
    
    private String getColored(String text, String colorCode) {
        if (this.noColor) {
            return text;
        }
        return colorCode + text + "\033[0m";
    }
}