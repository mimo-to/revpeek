package com.revpeek.output;

import com.revpeek.analytics.AnalyticsService;

public class HtmlRenderer {
    
    public String render(AnalyticsService.CommitStats stats) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n<head>\n");
        html.append("  <title>RevPeek Analytics Report</title>\n");
        html.append(" <meta charset=\"UTF-8\">\n");
        html.append("  <style>\n");
        html.append("    body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }\n");
        html.append("    .container { max-width: 1200px; margin: 0 auto; background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0.1); }\n");
        html.append("    h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }\n");
        html.append("    .summary { display: flex; gap: 20px; margin-bottom: 30px; }\n");
        html.append("    .summary-box { flex: 1; background-color: #ecf0f1; padding: 15px; border-radius: 5px; text-align: center; }\n");
        html.append("    .summary-value { font-size: 2em; font-weight: bold; color: #3498db; }\n");
        html.append("    .section { margin: 25px 0; }\n");
        html.append("    h2 { color: #34495e; border-left: 4px solid #3498db; padding-left: 10px; }\n");
        html.append("    table { width: 100%; border-collapse: collapse; margin-top: 10px; }\n");
        html.append("    th, td { padding: 10px; text-align: left; border-bottom: 1px solid #ddd; }\n");
        html.append("    th { background-color: #3498db; color: white; }\n");
        html.append("    tr:nth-child(even) { background-color: #f2f2f2; }\n");
        html.append("    .bar { display: inline-block; height: 20px; background-color: #3498db; vertical-align: middle; }\n");
        html.append("    .footer { margin-top: 30px; text-align: center; color: #7f8c8d; font-size: 0.9em; }\n");
        html.append("  </style>\n");
        html.append("</head>\n<body>\n");
        html.append("<div class=\"container\">\n");
        
        // Header
        html.append("  <h1>📊 RevPeek Analytics Report</h1>\n");
        
        // Summary
        html.append("  <div class=\"summary\">\n");
        html.append("    <div class=\"summary-box\">\n");
        html.append("      <div class=\"summary-value\">" + stats.totalCommits + "</div>\n");
        html.append("      <div>Total Commits</div>\n");
        html.append("    </div>\n");
        html.append("    <div class=\"summary-box\">\n");
        html.append("      <div class=\"summary-value\">" + stats.authorCommits.size() + "</div>\n");
        html.append("      <div>Contributors</div>\n");
        html.append("    </div>\n");
        html.append("    <div class=\"summary-box\">\n");
        html.append("      <div class=\"summary-value\">" + (stats.firstCommitDate != null ? stats.firstCommitDate : "N/A") + "</div>\n");
        html.append("      <div>First Commit</div>\n");
        html.append("    </div>\n");
        html.append("    <div class=\"summary-box\">\n");
        html.append("      <div class=\"summary-value\">" + (stats.lastCommitDate != null ? stats.lastCommitDate : "N/A") + "</div>\n");
        html.append("      <div>Last Commit</div>\n");
        html.append("    </div>\n");
        html.append(" </div>\n");
        
        // Top Authors
        html.append("  <div class=\"section\">\n");
        html.append("    <h2>👥 Top Authors</h2>\n");
        html.append("    <table>\n");
        html.append("      <thead><tr><th>Author</th><th>Commits</th><th>Activity</th></tr></thead>\n");
        html.append("      <tbody>\n");
        
        // Sort authors by commit count
        stats.authorCommits.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(entry -> {
                int maxCommits = stats.authorCommits.values().stream().mapToInt(Integer::intValue).max().orElse(1);
                int barWidth = Math.max(1, (entry.getValue() * 100) / maxCommits);
                html.append("        <tr>\n");
                html.append("          <td>" + escapeHtml(entry.getKey()) + "</td>\n");
                html.append("          <td>" + entry.getValue() + "</td>\n");
                html.append("          <td><div class=\"bar\" style=\"width: " + barWidth + "px;\"></div></td>\n");
                html.append("        </tr>\n");
            });
        
        html.append("      </tbody>\n");
        html.append("    </table>\n");
        html.append(" </div>\n");
        
        // File Types
        html.append("  <div class=\"section\">\n");
        html.append("    <h2>📁 File Types</h2>\n");
        html.append("    <table>\n");
        html.append("      <thead><tr><th>File Type</th><th>Files</th><th>Distribution</th></tr></thead>\n");
        html.append("      <tbody>\n");
        
        // Sort file types by count
        stats.fileTypeDistribution.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(entry -> {
                int maxFiles = stats.fileTypeDistribution.values().stream().mapToInt(Integer::intValue).max().orElse(1);
                int barWidth = Math.max(1, (entry.getValue() * 100) / maxFiles);
                html.append("        <tr>\n");
                html.append("          <td>" + escapeHtml(entry.getKey()) + "</td>\n");
                html.append("          <td>" + entry.getValue() + "</td>\n");
                html.append("          <td><div class=\"bar\" style=\"width: " + barWidth + "px;\"></div></td>\n");
                html.append("        </tr>\n");
            });
        
        html.append("      </tbody>\n");
        html.append("    </table>\n");
        html.append("  </div>\n");
        
        html.append("  <div class=\"footer\">\n");
        html.append("    Generated by RevPeek - A terminal-first Git analytics CLI tool\n");
        html.append("  </div>\n");
        html.append("</div>\n");
        html.append("</body>\n</html>");
        
        return html.toString();
    }
    
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&")
                   .replace("<", "<")
                   .replace(">", ">")
                   .replace("\"", """)
                   .replace("'", "&#x27;");
    }
}