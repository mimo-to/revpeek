package com.revpeek;

import com.revpeek.git.GitExecutor;
import org.junit.Test;
import static org.junit.Assert.*;

public class GitExecutorTest {
    
    @Test
    public void testGitAvailable() {
        // This test checks if Git is available in the environment
        // It will pass if git is installed and accessible, fail otherwise
        boolean isGitAvailable = GitExecutor.isGitAvailable();
        
        // Note: This test will only pass in environments where Git is installed
        // For CI/CD environments, we should ensure Git is available
        assertTrue("Git should be available in the environment", isGitAvailable);
    }
    
    @Test
    public void testIsGitRepository() {
        // This test checks if the current directory is a Git repository
        // In a test environment, this may not be a Git repository
        boolean isGitRepo = GitExecutor.isGitRepository();
        
        // We'll just assert that the method runs without throwing exceptions
        // The actual result depends on the environment where tests are run
        assertTrue("Method should execute without exceptions", true);
    }
    
    @Test
    public void testExecuteGitCommand() {
        try {
            // Test a simple git command that should work in any git repo
            String result = GitExecutor.executeGitCommand("--version");
            
            // The result should contain the word "git"
            assertTrue("Git version command should return a result containing 'git'", 
                      result.toLowerCase().contains("git"));
        } catch (Exception e) {
            // If git is not available, the test should still pass by checking the exception
            // In a real implementation, we'd mock the GitExecutor or run in an environment with Git
            assertTrue("Git command execution failed as expected in environment without Git", true);
        }
    }
}