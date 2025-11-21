package com.revpeek.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(
    name = "revpeek",
    description = "A terminal-first Git analytics CLI tool",
    mixinStandardHelpOptions = true,
    version = "1.0.0",
    subcommands = { AnalyzeCommand.class, ExportCommand.class, CompletionCommand.class }
)
public class RevPeekCommand implements Callable<Integer> {

    @Option(names = {"-v", "--verbose"}, description = "Enable verbose output")
    boolean verbose;

    @Option(names = {"--no-color"}, description = "Disable colored output")
    boolean noColor;

    @Option(names = {"--json"}, description = "Output in JSON format (takes precedence over other format options)")
    boolean json;

    @Override
    public Integer call() throws Exception {
        // Default: run analyze
        return new AnalyzeCommand().call();
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new RevPeekCommand())
            .setUsageHelpWidth(80)  // Keep it narrow for terminals
            .execute(args);
        System.exit(exitCode);
    }
}