package com.revpeek.git;

import com.revpeek.git.model.Commit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class GitLogParser {
    
    public static List<Commit> parseGitLog(String gitLogOutput) {
        List<Commit> commits = new ArrayList<>();
        
        String[] lines = gitLogOutput.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            
            // Parse commit with format: hash|author_name|author_email|date|message
            String[] parts = line.split(Pattern.quote("|"), 5);
            if (parts.length >= 5) {
                String hash = parts[0].trim();
                String authorName = parts[1].trim();
                String date = parts[3].trim();
                String message = parts[4].trim();
                
                Commit commit = new Commit(hash, authorName, date, message); // Use just the name as author
                commits.add(commit);
            }
        }
        
        return commits;
    }
    
    public static List<Commit> getCommits(String since, String until) throws Exception {
        // Format: git log --pretty=format:"%H|%an|%ae|%ad|%s"
        List<String> cmd = new ArrayList<>();
        cmd.add("log");
        cmd.add("--pretty=format:%H|%an|%ae|%ad|%s");
        cmd.add("--date=short");  // ← critical
        
        if (since != null && !since.isEmpty()) {
            cmd.add("--since=" + since);
        }
        if (until != null && !until.isEmpty()) {
            cmd.add("--until=" + until);
        }
        
        String[] cmdArray = cmd.toArray(new String[0]);
        String gitOutput = GitExecutor.executeGitCommand(cmdArray);
        return parseGitLog(gitOutput);
    }
    
    public static Map<String, Integer> getCommitCountByAuthor(List<Commit> commits) {
        Map<String, Integer> authorCounts = new HashMap<>();
        for (Commit commit : commits) {
            String authorName = commit.getAuthor();
            authorCounts.put(authorName, authorCounts.getOrDefault(authorName, 0) + 1);
        }
        return authorCounts;
    }
    
    public static Map<String, Integer> getFileTypeDistribution(List<Commit> commits) throws Exception {
        Map<String, Integer> fileTypeCounts = new HashMap<>();
        
        // Get file changes for each commit using --numstat
        for (Commit commit : commits) {
            try {
                String numstat = GitExecutor.executeGitCommand("show", "--numstat", "--pretty=format:", commit.getHash());
                // Output: "12\t3\tsrc/App.java\n5\t0\tREADME.md"
                String[] lines = numstat.split("\n");
                for (String line : lines) {
                    String[] parts = line.split("\t");
                    if (parts.length >= 3) {
                        String file = parts[2];
                        String ext = getFileExtension(file);
                        fileTypeCounts.merge(ext, 1, Integer::sum);
                    }
                }
            } catch (Exception e) {
                // If we can't get file changes for a commit, skip it
                continue;
            }
        }
        
        return fileTypeCounts;
    }
    
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "no_ext"; // for files without extensions
    }
}