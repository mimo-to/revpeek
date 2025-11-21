# RevPeek Installer Script for Windows
# Downloads and installs RevPeek to the user's system

param(
    [string]$InstallPath = "$env:LOCALAPPDATA\revpeek",
    [string]$Version = "latest"
)

Write-Host "Installing RevPeek..." -ForegroundColor Green

# Create installation directory
if (!(Test-Path $InstallPath)) {
    New-Item -ItemType Directory -Path $InstallPath -Force | Out-Null
}

# Determine the latest version if 'latest' is specified
$downloadUrl = "https://github.com/mimo-to/revpeek/releases/latest/download/revpeek.jar"
if ($Version -ne "latest") {
    $downloadUrl = "https://github.com/mimo-to/revpeek/releases/download/v$Version/revpeek.jar"
}

Write-Host "Downloading RevPeek from: $downloadUrl" -ForegroundColor Yellow

try {
    # Download the JAR file
    $jarPath = Join-Path $InstallPath "revpeek.jar"
    Invoke-WebRequest -Uri $downloadUrl -OutFile $jarPath -UseBasicParsing
    
    Write-Host "Downloaded RevPeek successfully!" -ForegroundColor Green
    
    # Create or update the PowerShell script in a location that's likely in PATH
    $scriptPath = Join-Path $InstallPath "revpeek.ps1"
    
    # Content for the launcher script
    $launcherScript = @"
# RevPeek PowerShell Launcher
# This script runs the RevPeek JAR file

param(
    [Parameter(ValueFromRemainingArguments=$true)]
    [string[]]$Arguments
)

# Find Java installation
`$javaCmd = Get-Command java -ErrorAction SilentlyContinue
if (`$null -eq `$javaCmd) {
    Write-Error "Java is not installed or not in PATH. Please install Java 17+."
    exit 1
}

# Use the JAR file in the same directory as this script
`$jarPath = Join-Path (Split-Path -Parent `$MyInvocation.MyCommand.Definition) "revpeek.jar"

if (!(Test-Path `$jarPath)) {
    Write-Error "revpeek.jar not found in installation directory."
    exit 1
}

# Execute the JAR with provided arguments
`$allArgs = @("-jar", `$jarPath) + `$Arguments
& java @allArgs
"@
    
    # Write the launcher script
    Set-Content -Path $scriptPath -Value $launcherScript
    
    Write-Host "Created launcher script at: $scriptPath" -ForegroundColor Green
    
    # Try to add the installation directory to PATH if it's not already there
    $currentPath = [System.Environment]::GetEnvironmentVariable("Path", "User")
    if ($currentPath -split ';' -notcontains $InstallPath) {
        $newPath = $currentPath + ";$InstallPath"
        [System.Environment]::SetEnvironmentVariable("Path", $newPath, "User")
        Write-Host "Added $InstallPath to your PATH. You may need to restart your terminal." -ForegroundColor Yellow
    } else {
        Write-Host "$InstallPath is already in your PATH." -ForegroundColor Green
    }
    
    # Copy the script to a location that's definitely in PATH for immediate use
    $userPath = [System.Environment]::GetEnvironmentVariable("Path", "User") -split ';'
    $commonBinPath = $null
    
    foreach ($path in $userPath) {
        if (Test-Path $path) {
            $testFile = Join-Path $path "test.txt"
            try {
                Set-Content -Path $testFile -Value "test" -ErrorAction Stop
                Remove-Item $testFile -ErrorAction Stop
                $commonBinPath = $path
                break
            } catch {
                # This path is not writable, try the next one
                continue
            }
        }
    }
    
    if ($commonBinPath) {
        $globalScriptPath = Join-Path $commonBinPath "revpeek.ps1"
        Copy-Item -Path $scriptPath -Destination $globalScriptPath -Force
        Write-Host "Copied revpeek.ps1 to $globalScriptPath for immediate use." -ForegroundColor Green
    }
    
    Write-Host "RevPeek installed successfully!" -ForegroundColor Green
    Write-Host "Run 'revpeek --help' to get started." -ForegroundColor Cyan
    
} catch {
    Write-Error "Failed to install RevPeek: $($_.Exception.Message)"
    exit 1
}