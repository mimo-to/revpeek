package com.revpeek.output;

import com.revpeek.analytics.AnalyticsService;

import java.util.Map;

public class JsonRenderer {
    
    public String render(AnalyticsService.CommitStats stats) {
        StringBuilder json = new StringBuilder();
        
        json.append("{\n");
        json.append(" \"summary\": {\n");
        json.append("    \"totalCommits\": ").append(stats.totalCommits).append(",\n");
        json.append("    \"contributors\": ").append(stats.authorCommits.size()).append(",\n");
        json.append("    \"firstCommitDate\": \"").append(escapeJson(stats.firstCommitDate != null ? stats.firstCommitDate : "N/A")).append("\",\n");
        json.append("    \"lastCommitDate\": \"").append(escapeJson(stats.lastCommitDate != null ? stats.lastCommitDate : "N/A")).append("\"\n");
        json.append(" },\n");
        
        json.append("  \"authors\": {\n");
        boolean firstAuthor = true;
        for (Map.Entry<String, Integer> entry : stats.authorCommits.entrySet()) {
            if (!firstAuthor) json.append(",\n");
            json.append("    \"").append(escapeJson(entry.getKey())).append("\": ").append(entry.getValue());
            firstAuthor = false;
        }
        json.append("\n },\n");
        
        json.append("  \"fileTypes\": {\n");
        boolean firstFileType = true;
        for (Map.Entry<String, Integer> entry : stats.fileTypeDistribution.entrySet()) {
            if (!firstFileType) json.append(",\n");
            json.append("    \"").append(escapeJson(entry.getKey())).append("\": ").append(entry.getValue());
            firstFileType = false;
        }
        json.append("\n },\n");
        
        json.append(" \"dailyCommits\": {\n");
        boolean firstDay = true;
        for (Map.Entry<String, Integer> entry : stats.dailyCommits.entrySet()) {
            if (!firstDay) json.append(",\n");
            json.append("    \"").append(escapeJson(entry.getKey())).append("\": ").append(entry.getValue());
            firstDay = false;
        }
        json.append("\n }\n");
        
        json.append("}");
        
        return json.toString();
    }
    
    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}