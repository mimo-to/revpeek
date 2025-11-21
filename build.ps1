# build.ps1 — runs in project root
Write-Host "🚀 Building RevPeek..." -ForegroundColor Cyan

# Clean
if (Test-Path build) { Remove-Item -Recurse -Force build }
New-Item -ItemType Directory build | Out-Null

# Compile single file
Write-Host "▸ Compiling..." -ForegroundColor DarkGray
javac -d build -cp "lib/*" src/main/java/com/revpeek/cli/*.java src/main/java/com/revpeek/git/*.java src/main/java/com/revpeek/git/model/*.java src/main/java/com/revpeek/analytics/*.java src/main/java/com/revpeek/output/*.java

if ($LASTEXITCODE -ne 0) { exit 1 }

# Create JAR
Write-Host "▸ Packaging JAR..." -ForegroundColor DarkGray
Set-Content build/MANIFEST.MF "Main-Class: com.revpeek.cli.RevPeekCommand`r`n"
jar -c -m build/MANIFEST.MF -f build/revpeek.jar -C build .

# Shade picocli
$PICOLI_JAR = "lib/picocli-4.7.5.jar"
if (!(Test-Path $PICOLI_JAR)) {
    Write-Host "⚠️ picocli not found — downloading..." -ForegroundColor Yellow
    if (!(Test-Path lib)) { New-Item -ItemType Directory lib | Out-Null }
    Invoke-WebRequest "https://repo1.maven.org/maven2/info/picocli/picocli/4.7.5/picocli-4.7.5.jar" -OutFile $PICOLI_JAR
}

Write-Host "▸ Shading picocli..." -ForegroundColor DarkGray
# Extract picocli to temp directory and add to main JAR
$tempDir = "temp_picocli"
if (Test-Path $tempDir) { Remove-Item -Path $tempDir -Recurse -Force }
New-Item -ItemType Directory -Path $tempDir | Out-Null
Expand-Archive -Path $PICOLI_JAR -DestinationPath $tempDir
Get-ChildItem -Path $tempDir -Recurse -File | ForEach-Object {
    $relativePath = Resolve-Path -Path $_.FullName -Relative
    $relativePath = $relativePath.Substring(1)  # Remove leading .\
    jar -uf build/revpeek.jar -C $tempDir $relativePath
}
Remove-Item -Path $tempDir -Recurse -Force

Write-Host "(Success) Run with: java -jar build/revpeek.jar" -ForegroundColor Green