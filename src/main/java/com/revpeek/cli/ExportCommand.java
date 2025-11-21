package com.revpeek.cli;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;
import com.revpeek.git.GitExecutor;
import com.revpeek.git.GitLogParser;
import com.revpeek.analytics.AnalyticsService;
import com.revpeek.output.HtmlRenderer;
import com.revpeek.output.MarkdownRenderer;
import com.revpeek.output.JsonRenderer;

@Command(name = "export", description = "Export analysis to various formats")
public class ExportCommand implements Callable<Integer> {

    @Option(names = {"-f", "--format"}, description = "Export format (html, markdown, json)", required = true)
    String format;

    @Option(names = {"-o", "--output"}, description = "Output file [default: report.html, report.md, or stdout for JSON]")
    String output;

    @Option(names = {"--since"}, description = "Export commits since this date")
    String since;

    @Option(names = {"--until"}, description = "Export commits until this date")
    String until;

    @Override
    public Integer call() throws Exception {
        // Check if we're in a Git repository
        if (!GitExecutor.isGitRepository()) {
            System.err.println("Error: Not in a Git repository");
            return 1;
        }

        // Check if Git is available
        if (!GitExecutor.isGitAvailable()) {
            System.err.println("Error: Git is not available in PATH");
            return 1;
        }

        try {
            // Set default output file if not specified
            if (output == null) {
                switch (format.toLowerCase()) {
                    case "html":
                        output = "report.html";
                        break;
                    case "markdown":
                        output = "report.md";
                        break;
                    case "json":
                        // For JSON, default to stdout if no output specified
                        break;
                    default:
                        System.err.println("Unknown format: " + format);
                        return 1;
                }
            }

            // Get commits from Git
            var commits = GitLogParser.getCommits(since, until);
            
            // Generate analytics
            var stats = AnalyticsService.generateStats(commits);
            
            String outputContent;
            switch (format.toLowerCase()) {
                case "html":
                    outputContent = new HtmlRenderer().render(stats);
                    break;
                case "markdown":
                    outputContent = new MarkdownRenderer().render(stats);
                    break;
                case "json":
                    outputContent = new JsonRenderer().render(stats);
                    break;
                default:
                    System.err.println("Unknown format: " + format);
                    return 1;
            }
            
            // Output to file or stdout
            if (output.equals("-") || output.toLowerCase().equals("stdout")) {
                System.out.println(outputContent);
            } else {
                java.nio.file.Files.write(java.nio.file.Paths.get(output), outputContent.getBytes());
                System.out.println("Exported to " + output);
            }
            
            return 0;
        } catch (Exception e) {
            System.err.println("Error exporting data: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
}