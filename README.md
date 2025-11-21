# RevPeek - Terminal-First Git Analytics CLI Tool

RevPeek is a terminal-first, zero-GUI Git analytics CLI tool that allows developers to instantly get smart, visual insights about their commit history with no setup, no server, and no GUI.

## 🚀 Features

- **Terminal-First**: Beautiful visual reports directly in your terminal with ASCII charts, tables, and color coding
- **Zero Setup**: Just install and run - no configuration required
- **Multi-Format Output**: Terminal, HTML, Markdown, and JSON formats
- **Cross-Platform**: Works on Windows, macOS, and Linux
- **Fast**: Optimized for performance with large repositories

## 📦 Installation

### Windows (PowerShell)
```powershell
# Option 1: Using PowerShell installer
iwr -useb https://revpeek.dev/install.ps1 | iex

# Option 2: Manual installation
# Download revpeek.ps1 and place it in your PATH
```

### Unix/Linux/macOS
```bash
# Using curl (one-liner installation)
curl -L https://revpeek.dev/install | sh

# Or using wget
wget -O - https://revpeek.dev/install | sh
```

### Manual Installation
1. Download the latest `revpeek.jar` from [releases](https://github.com/mimo-to/revpeek/releases)
2. Create a launcher script in your PATH:
   - On Windows: `revpeek.ps1`
   - On Unix: `revpeek` (executable shell script)
3. Make sure Java 17+ is installed and in your PATH

## 🚀 Quick Start

RevPeek: Git history, visualized in your terminal — no fluff, no GUI, just insights.

```bash
curl -L revpeek.dev/install | sh && revpeek
```

## 🛠️ Usage

### Basic Analysis
```bash
# Analyze current repository (default format: terminal)
revpeek
# or
revpeek analyze

# With specific output format
revpeek analyze --format=html > report.html
revpeek analyze --format=markdown > report.md
revpeek --json  # Output in JSON format (takes precedence over other format options)
```

### Advanced Options
```bash
# Analyze commits within a date range
revpeek analyze --since=2023-01-01 --until=2023-12-31

# Export to specific format
revpeek export --format=html --output=report.html

# Generate shell completions
revpeek completion --shell=bash > revpeek-completion.bash
```

### PowerShell Example
```powershell
# Analyze and filter results in PowerShell
revpeek --json | ConvertFrom-Json | Select-Object -ExpandProperty authors | Where-Object { $_.value -gt 10 }
```

## 📊 Analytics Provided

- **Summary Statistics**: Total commits, contributors, date range
- **Top Authors**: Commits by author with visual bars
- **Commit Activity**: Daily/hourly commit patterns
- **File Type Distribution**: Programming languages and file extensions used
- **Timeline Analysis**: Commit frequency over time

## ⌨️ Shell Completions

Generate and install shell completions:

```bash
# Bash
revpeek completion --shell=bash > /etc/bash_completion.d/revpeek

# Zsh
revpeek completion --shell=zsh > /usr/local/share/zsh/site-functions/_revpeek

# PowerShell
# Add the output to your PowerShell profile
```

## 🔧 Development

### Prerequisites
- Java 17+
- Git

### Building from Source
```bash
# Clone the repository
git clone https://github.com/mimo-to/revpeek.git
cd revpeek

# Build the JAR file
./build.ps1 # On Windows
# or
./gradlew build  # If using Gradle (not currently implemented)

# Run directly
java -jar build/revpeek.jar --help
```

### Project Structure
```
revpeek/
├── src/main/java/com/revpeek/          # Source code
│   ├── cli/                           # Command line interface
│   ├── git/                           # Git operations
│   ├── analytics/                     # Analytics processing
│   └── output/                        # Output rendering
├── bin/                               # Executable scripts
├── scripts/                           # Installation scripts
├── build.ps1                          # Windows build script
└── .github/workflows/ci.yml           # CI/CD pipeline
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Add tests if applicable
5. Commit your changes (`git commit -m 'Add amazing feature'`)
6. Push to the branch (`git push origin feature/amazing-feature`)
7. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Built with [picocli](https://picocli.info/) for command-line parsing
- Inspired by tools like `htop` for terminal-first design
- Thanks to all contributors who help make RevPeek better