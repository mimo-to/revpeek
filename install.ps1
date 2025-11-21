$ErrorActionPreference = "Stop"

# Define install paths
$installDir = "$env:USERPROFILE\revpeek_bin"
$jarUrl = "https://github.com/mimo-to/revpeek/releases/latest/download/revpeek.jar"
$jarDest = "$installDir\revpeek.jar"
$batDest = "$installDir\revpeek.bat"

Write-Host "🚀 Installing RevPeek globally..." -ForegroundColor Cyan

# 1. Create Directory
if (-not (Test-Path $installDir)) {
    New-Item -ItemType Directory -Force -Path $installDir | Out-Null
    Write-Host "Created directory: $installDir" -ForegroundColor Green
}

# 2. Download JAR
Write-Host "Downloading latest release from GitHub..." -ForegroundColor Cyan
try {
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $jarUrl -OutFile $jarDest -UserAgent "NativeHost"
    Write-Host "Download complete." -ForegroundColor Green
} catch {
    Write-Error "Failed to download RevPeek. Please check your internet connection or try again later."
    exit 1
}

# 3. Create Batch Wrapper
$batContent = "@echo off`r`njava -jar ""%~dp0revpeek.jar"" %*"
Set-Content -Path $batDest -Value $batContent -Encoding ASCII
Write-Host "Created wrapper script." -ForegroundColor Green

# 4. Add to PATH (Persistent)
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
if ($currentPath -notlike "*$installDir*") {
    $newPath = "$currentPath;$installDir"
    [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
    Write-Host "Added $installDir to User PATH." -ForegroundColor Green
    Write-Host "⚠️  PLEASE RESTART YOUR TERMINAL for the 'revpeek' command to work." -ForegroundColor Yellow
} else {
    Write-Host "Path already configured." -ForegroundColor Green
}

Write-Host "`n✅ Installation Complete! You can now run 'revpeek' from any terminal." -ForegroundColor Cyan
