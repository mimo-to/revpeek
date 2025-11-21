# RevPeek - Terminal-First Git Analytics

> **Instant, beautiful Git insights directly in your terminal.**

RevPeek is a zero-config, terminal-first analytics tool for Git repositories. It parses your commit history to provide visual insights about contributors, activity patterns, and file distribution—all without leaving your command line.

![RevPeek Demo](https://via.placeholder.com/800x400?text=RevPeek+Terminal+Demo)

## 🚀 Features

- **Zero Setup**: No servers, no databases, no config files. Just run it.
- **Terminal-First**: Beautiful ASCII charts and colored output.
- **Fast**: Optimized single-pass parsing for large repositories.
- **Multi-Format**: Export reports to HTML, Markdown, JSON, or plain text.
- **Cross-Platform**: Runs anywhere Java runs (Windows, macOS, Linux).

## 📦 Installation & Usage

### 🚀 Instant Run (One-Liner)
You can download and run RevPeek instantly without installing anything (requires Java 17+):

**Unix/Linux/macOS:**
```bash
curl -sL https://github.com/mimo-to/revpeek/releases/latest/download/revpeek.jar -o revpeek.jar && java -jar revpeek.jar
```

**Windows (PowerShell):**
```powershell
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri "https://github.com/mimo-to/revpeek/releases/latest/download/revpeek.jar" -OutFile "revpeek.jar"; java -jar revpeek.jar
```

### Manual Download
1. Download `revpeek.jar` from the [Latest Release](https://github.com/mimo-to/revpeek/releases/latest).
2. Run it in your terminal:
   ```bash
   java -jar revpeek.jar
   ```

### Optional: Add to PATH
To run `revpeek` from anywhere, create a simple alias or wrapper script:

**Bash/Zsh:**
```bash
alias revpeek='java -jar /path/to/revpeek.jar'
```

**PowerShell:**
```powershell
Set-Alias -Name revpeek -Value "java -jar C:\path\to\revpeek.jar"
```

## �️ Usage

Navigate to any Git repository and run:

```bash
revpeek
```

### Common Commands

| Command | Description |
|---------|-------------|
| `revpeek` | Analyze the current repository (default) |
| `revpeek analyze --since=2023-01-01` | Analyze commits from a specific date |
| `revpeek analyze --format=html -o report.html` | Generate a visual HTML report |
| `revpeek --json` | Output raw JSON data for custom tooling |

### Examples

**Analyze the last 30 days:**
```bash
revpeek analyze --since="30 days ago"
```

**Export a Markdown report for documentation:**
```bash
revpeek analyze --format=markdown > ANALYTICS.md
```

**CI/CD Integration (JSON output):**
```bash
revpeek --json | jq '.totalCommits'
```

## 🔧 Building from Source

Requirements: Java 17+

```bash
git clone https://github.com/mimo-to/revpeek.git
cd revpeek

# Windows
./build.ps1

# Unix/Linux
javac -d build -cp "lib/*" src/main/java/com/revpeek/cli/*.java ...
jar -c -f build/revpeek.jar -C build .
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.