#!/bin/bash

# RevPeek Installer Script for Unix/Linux/macOS
# Downloads and installs RevPeek to the user's system

set -e  # Exit on any error

# Default installation directory
INSTALL_DIR="${HOME}/.local/bin"
JAR_FILE="revpeek.jar"
SCRIPT_NAME="revpeek"

# Parse command line options
while [[ $# -gt 0 ]]; do
    case $1 in
        -d|--dir)
            INSTALL_DIR="$2"
            shift 2
            ;;
        -v|--version)
            VERSION="$2"
            shift 2
            ;;
        -h|--help)
            echo "Usage: $0 [OPTIONS]"
            echo "Options:"
            echo " -d, --dir DIR     Installation directory (default: ${HOME}/.local/bin)"
            echo "  -v, --version VER Install specific version (default: latest)"
            echo " -h, --help        Show this help message"
            exit 0
            ;;
        *)
            echo "Unknown option: $1"
            exit 1
            ;;
    esac
done

echo "Installing RevPeek to: $INSTALL_DIR"

# Create installation directory if it doesn't exist
mkdir -p "$INSTALL_DIR"

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH. Please install Java 17+."
    exit 1
fi

# Determine download URL
if [ -z "$VERSION" ] || [ "$VERSION" = "latest" ]; then
    DOWNLOAD_URL="https://github.com/mimo-to/revpeek/releases/latest/download/revpeek.jar"
else
    DOWNLOAD_URL="https://github.com/mimo-to/revpeek/releases/download/v$VERSION/revpeek.jar"
fi

echo "Downloading RevPeek from: $DOWNLOAD_URL"

# Download the JAR file
if command -v curl &> /dev/null; then
    curl -L -o "$INSTALL_DIR/$JAR_FILE" "$DOWNLOAD_URL"
elif command -v wget &> /dev/null; then
    wget -O "$INSTALL_DIR/$JAR_FILE" "$DOWNLOAD_URL"
else
    echo "Error: Neither curl nor wget is available to download the file."
    exit 1
fi

echo "Downloaded RevPeek successfully!"

# Create the launcher script
LAUNCHER_PATH="$INSTALL_DIR/$SCRIPT_NAME"
cat > "$LAUNCHER_PATH" << 'EOF'
#!/bin/bash

# RevPeek Launcher Script
# This script runs the RevPeek JAR file

JAR_PATH="$(dirname "$0")/revpeek.jar"

if [ ! -f "$JAR_PATH" ]; then
    echo "Error: revpeek.jar not found in the same directory as this script."
    exit 1
fi

# Execute the JAR with provided arguments
exec java -jar "$JAR_PATH" "$@"
EOF

# Make the launcher script executable
chmod +x "$LAUNCHER_PATH"

echo "Created executable launcher at: $LAUNCHER_PATH"

# Check if installation directory is in PATH
if [[ ":$PATH:" != *":$INSTALL_DIR:"* ]]; then
    echo ""
    echo "Warning: $INSTALL_DIR is not in your PATH."
    echo "To use RevPeek from anywhere, add this line to your shell configuration file (~/.bashrc, ~/.zshrc, etc.):"
    echo " export PATH=\"\$PATH:$INSTALL_DIR\""
    echo ""
    echo "Then reload your shell configuration or start a new terminal session."
else
    echo "RevPeek installed successfully!"
    echo "Run 'revpeek --help' to get started."
fi