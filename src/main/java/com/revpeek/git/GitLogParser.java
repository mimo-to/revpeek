package com.revpeek.git;

import com.revpeek.git.model.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GitLogParser {
    
    // Separator used in git log format
    private static final String SEPARATOR = "|";
    
    public static List<Commit> getCommits(String since, String until) throws Exception {
        // Format: hash|author_name|author_email|date|message
        // We also add --numstat to get file changes in the same command
        List<String> cmd = new ArrayList<>();
        cmd.add("log");
        cmd.add("--pretty=format:COMMIT:%H|%an|%ae|%ad|%s");
        cmd.add("--date=short");
        cmd.add("--numstat");
        
        if (since != null && !since.isEmpty()) {
            cmd.add("--since=" + since);
        }
        if (until != null && !until.isEmpty()) {
            cmd.add("--until=" + until);
        }
        
        String[] cmdArray = cmd.toArray(new String[0]);
        String gitOutput = GitExecutor.executeGitCommand(cmdArray);
        return parseGitLogWithNumstat(gitOutput);
    }
    
    private static List<Commit> parseGitLogWithNumstat(String gitOutput) {
        List<Commit> commits = new ArrayList<>();
        String[] lines = gitOutput.split("\n");
        
        Commit currentCommit = null;
        
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            if (line.startsWith("COMMIT:")) {
                // New commit found
                if (currentCommit != null) {
                    commits.add(currentCommit);
                }
                
                // Parse commit header: COMMIT:hash|author|email|date|message
                String content = line.substring(7); // Remove "COMMIT:"
                String[] parts = content.split(Pattern.quote(SEPARATOR), 5);
                
                if (parts.length >= 5) {
                    String hash = parts[0].trim();
                    String authorName = parts[1].trim();
                    String date = parts[3].trim();
                    String message = parts[4].trim();
                    
                    currentCommit = new Commit(hash, authorName, date, message);
                }
            } else if (currentCommit != null) {
                // Parse numstat line: added deleted filename
                // Example: 10 5 src/Main.java
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    String filename = parts[2];
                    currentCommit.addFileChange(filename);
                }
            }
        }
        
        // Add the last commit
        if (currentCommit != null) {
            commits.add(currentCommit);
        }
        
        return commits;
    }
    
    public static Map<String, Integer> getCommitCountByAuthor(List<Commit> commits) {
        Map<String, Integer> authorCounts = new HashMap<>();
        for (Commit commit : commits) {
            String authorName = commit.getAuthor();
            authorCounts.put(authorName, authorCounts.getOrDefault(authorName, 0) + 1);
        }
        return authorCounts;
    }
    
    public static Map<String, Integer> getFileTypeDistribution(List<Commit> commits) {
        Map<String, Integer> fileTypeCounts = new HashMap<>();
        
        for (Commit commit : commits) {
            for (String file : commit.getFiles()) {
                String ext = getFileExtension(file);
                fileTypeCounts.merge(ext, 1, Integer::sum);
            }
        }
        
        return fileTypeCounts;
    }
    
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "no_ext";
    }
}