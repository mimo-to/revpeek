package com.revpeek.cli;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;
import com.revpeek.git.GitExecutor;
import com.revpeek.git.GitLogParser;
import com.revpeek.analytics.AnalyticsService;
import com.revpeek.output.TerminalRenderer;
import com.revpeek.output.HtmlRenderer;
import com.revpeek.output.MarkdownRenderer;
import com.revpeek.output.JsonRenderer;

@Command(name = "analyze", description = "Analyze the current Git repository")
public class AnalyzeCommand implements Callable<Integer> {

    @Option(names = {"-f", "--format"}, description = "Output format (terminal, html, markdown)", defaultValue = "terminal")
    String format;

    @Option(names = {"-o", "--output"}, description = "Output file (if not specified, prints to stdout)")
    String output;

    @Option(names = {"--since"}, description = "Analyze commits since this date")
    String since;

    @Option(names = {"--until"}, description = "Analyze commits until this date")
    String until;

    @Option(names = {"--no-color"}, description = "Disable colored output")
    boolean noColor;

    @Option(names = {"--json"}, description = "Output in JSON format (takes precedence over other format options)")
    boolean json;

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
            // Get commits from Git
            var commits = GitLogParser.getCommits(since, until);
            
            // Generate analytics
            var stats = AnalyticsService.generateStats(commits);
            
            // Determine output format (JSON flag takes precedence)
            String actualFormat = json ? "json" : format;
            
            String outputContent;
            switch (actualFormat.toLowerCase()) {
                case "terminal":
                    TerminalRenderer renderer = new TerminalRenderer(noColor);
                    if (output != null) {
                        // If output file is specified, still render to terminal but also save to file
                        renderer.render(stats);
                        // For file output, we usually want no color codes unless explicitly requested
                        // But here we'll respect the noColor flag or default to stripping colors for files if not specified
                        // For simplicity, we'll just use the same noColor setting
                        outputContent = renderer.renderToString(stats);
                    } else {
                        renderer.render(stats);
                        return 0;
                    }
                    break;
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
                    System.err.println("Unknown format: " + actualFormat);
                    return 1;
            }
            
            // Output to file or stdout
            if (output != null) {
                java.nio.file.Files.write(java.nio.file.Paths.get(output), outputContent.getBytes());
                System.out.println("Output written to " + output);
            } else {
                System.out.println(outputContent);
            }
            
            return 0;
        } catch (Exception e) {
            System.err.println("Error analyzing repository: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }
    

}