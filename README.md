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
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri "https://github.com/mimo-to/revpeek/releases/latest/download/revpeek.jar" -OutFile "revpeek.jar" -UserAgent "NativeHost"; java -jar revpeek.jar
```

### Manual Download
1. Download `revpeek.jar` from the [Latest Release](https://github.com/mimo-to/revpeek/releases/latest).
2. Run it in your terminal:
   ```bash
   java -jar revpeek.jar
   ```

### 🌍 Global Installation (Recommended)
To install `revpeek` globally so you can run it from any folder:

**Windows (PowerShell):**
Copy and run this command:
```powershell
powershell -c "irm https://raw.githubusercontent.com/mimo-to/revpeek/main/install.ps1 | iex"
```

**Unix/Linux/macOS:**
```bash
curl -sL https://github.com/mimo-to/revpeek/releases/latest/download/revpeek.jar -o revpeek.jar
sudo mv revpeek.jar /usr/local/bin/revpeek-jar
echo '#!/bin/bash' | sudo tee /usr/local/bin/revpeek
echo 'java -jar /usr/local/bin/revpeek-jar "$@"' | sudo tee -a /usr/local/bin/revpeek
sudo chmod +x /usr/local/bin/revpeek
```

## 🛠️ Usage

Once installed, navigate to any Git repository and run:

```bash
revpeek
```

### 🔍 Commands & Options

**1. Analyze (Default)**
Analyze the current repository and show a summary in the terminal.
```bash
revpeek analyze
```
*Options:*
*   `--since="2023-01-01"`: Analyze commits starting from a date.
*   `--until="2023-12-31"`: Analyze commits up to a date.
*   `--no-color`: Disable colored output.

**2. Export Reports**
Generate reports in different formats (HTML, Markdown, JSON).
```bash
# Generate a visual HTML report
revpeek export --format=html -o report.html

# Generate a Markdown report for documentation
revpeek export --format=markdown -o ANALYTICS.md

# Output raw JSON (great for scripts)
revpeek export --format=json
```

**3. JSON Output (Quick)**
Get raw JSON output instantly for integration with other tools (like `jq`).
```bash
revpeek --json
```

**4. Help**
See all available commands and options.
```bash
revpeek --help
```

### 💡 Examples

**Analyze the last 30 days:**
```bash
revpeek analyze --since="30 days ago"
```

**CI/CD Integration (Check commit count):**
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

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---
**RevPeek** - A Git Analytics CLI Tool by [mimo-to](https://github.com/mimo-to)