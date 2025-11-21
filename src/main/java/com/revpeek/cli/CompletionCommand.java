package com.revpeek.cli;

import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

@Command(name = "completion", description = "Generate shell completions")
public class CompletionCommand implements Callable<Integer> {

    @Option(names = {"--shell"}, description = "Shell type (bash, zsh, powershell) [default: auto-detect]")
    String shell;

    @Override
    public Integer call() throws Exception {
        // Auto-detect shell if not specified
        if (shell == null || shell.isEmpty()) {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                shell = "powershell";
            } else if (os.contains("mac") || os.contains("darwin")) {
                shell = "zsh";
            } else {
                shell = "bash";
            }
        }

        String completionScript = generateCompletionScript(shell.toLowerCase());
        
        System.out.println(completionScript);
        return 0;
    }
    
    private String generateCompletionScript(String shellType) {
        switch (shellType) {
            case "bash":
                return generateBashCompletion();
            case "zsh":
                return generateZshCompletion();
            case "powershell":
                return generatePowerShellCompletion();
            default:
                return "# Unsupported shell: " + shellType + "\n# Supported shells: bash, zsh, powershell";
        }
    }
    
    private String generateBashCompletion() {
        return "# Bash completion script for revpeek\n" +
               "_revpeek_completion() {\n" +
               "    local cur prev opts\n" +
               "    COMPREPLY=()\n" +
               "    cur=\"${COMP_WORDS[COMP_CWORD]\")\n" +
               "    prev=\"${COMP_WORDS[COMP_CWORD-1]}\")\n" +
               "    opts=\"--help --version --verbose --no-color --json analyze export completion\"\n" +
               "    \n" +
               "    if [[ ${cur} == -* ]]; then\n" +
               "        COMPREPLY=( $(compgen -W \"${opts}\" -- ${cur}) )\n" +
               "        return 0\n" +
               "    fi\n" +
               "    \n" +
               "    case \"${prev}\" in\n" +
               "        analyze)\n" +
               "            COMPREPLY=( $(compgen -W \"--format --output --since --until --no-color --json\" -- ${cur}) )\n" +
               "            ;;\n" +
               "        export)\n" +
               "            COMPREPLY=( $(compgen -W \"--format --output --since --until\" -- ${cur}) )\n" +
               "            ;;\n" +
               "        *)\n" +
               "            COMPREPLY=( $(compgen -W \"analyze export completion\" -- ${cur}) )\n" +
               "            ;;\n" +
               "    esac\n" +
               "}\n" +
               "\n" +
               "complete -F _revpeek_completion revpeek";
    }
    
    private String generateZshCompletion() {
        return "# Zsh completion script for revpeek\n" +
               "#compdef revpeek\n" +
               "\n" +
               "_revpeek() {\n" +
               "  local -a cmd\n" +
               "  cmd=(\n" +
               "    'analyze:Analyze the current Git repository'\n" +
               "    'export:Export analysis to various formats'\n" +
               "    'completion:Generate shell completions'\n" +
               "  )\n" +
               "  \n" +
               "  _arguments -C \\\n" +
               "    '--help[Show help message]' \\\n" +
               "    '--version[Show version information]' \\\n" +
               "    '--verbose[Enable verbose output]' \\\n" +
               "    '--no-color[Disable colored output]' \\\n" +
               "    '--json[Output in JSON format]' \\\n" +
               "    '1: :->cmds' \\\n" +
               "    '*::arg:->args'\n" +
               "  \n" +
               "  case $state in\n" +
               "    cmds)\n" +
               "      _describe 'command' cmd\n" +
               "      ;;\n" +
               "    args)\n" +
               "      case $words[2] in\n" +
               "        analyze)\n" +
               "          _arguments \\\n" +
               "            '--format[Output format]:format:(terminal html markdown)' \\\n" +
               "            '--output[Output file]:file:_files' \\\n" +
               "            '--since[Analyze commits since date]:date' \\\n" +
               "            '--until[Analyze commits until date]:date' \\\n" +
               "            '--no-color[Disable colored output]' \\\n" +
               "            '--json[Output in JSON format]'\n" +
               "          ;;\n" +
               "        export)\n" +
               "          _arguments \\\n" +
               "            '--format[Export format]:format:(html markdown json)' \\\n" +
               "            '--output[Output file]:file:_files' \\\n" +
               "            '--since[Export commits since date]:date' \\\n" +
               "            '--until[Export commits until date]:date'\n" +
               "          ;;\n" +
               "        completion)\n" +
               "          _arguments '--shell[Shell type]:shell:(bash zsh powershell)'\n" +
               "          ;;\n" +
               "      esac\n" +
               "      ;;\n" +
               "  esac\n" +
               "}\n" +
               "\n" +
               "_revpeek \"$@\"";
    }
    
    private String generatePowerShellCompletion() {
        return "# PowerShell completion script for revpeek\n" +
               "Register-ArgumentCompleter -CommandName 'revpeek' -ParameterName 'Command' -ScriptBlock {\n" +
               "    param($commandName, $parameterName, $wordToComplete, $commandAst, $fakeBoundParameters)\n" +
               "    \n" +
               "    $commands = @('analyze', 'export', 'completion')\n" +
               "    $commands | Where-Object { $_ -like \"$wordToComplete*\" } | ForEach-Object { New-Object System.Management.Automation.CompletionResult $_, $_, 'Parameter', $_ }\n" +
               "}\n" +
               "\n" +
               "Register-ArgumentCompleter -CommandName 'revpeek' -ParameterName 'Format' -ScriptBlock {\n" +
               "    param($commandName, $parameterName, $wordToComplete, $commandAst, $fakeBoundParameters)\n" +
               "    \n" +
               "    if ($fakeBoundParameters['Command'] -eq 'analyze') {\n" +
               "        $formats = @('terminal', 'html', 'markdown')\n" +
               "    } elseif ($fakeBoundParameters['Command'] -eq 'export') {\n" +
               "        $formats = @('html', 'markdown', 'json')\n" +
               "    }\n" +
               "    \n" +
               "    if ($formats) {\n" +
               "        $formats | Where-Object { $_ -like \"$wordToComplete*\" } | ForEach-Object { New-Object System.Management.Automation.CompletionResult $_, $_, 'Parameter', $_ }\n" +
               "    }\n" +
               "}";
    }
}