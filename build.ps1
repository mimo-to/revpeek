# build.ps1
Write-Host 'Building RevPeek...' -ForegroundColor Cyan

# Clean
if (Test-Path build) { Remove-Item -Recurse -Force build }
New-Item -ItemType Directory build | Out-Null

# Dependencies
$PICOLI_JAR = 'lib/picocli-4.7.5.jar'
if (!(Test-Path $PICOLI_JAR)) {
    Write-Host 'Downloading picocli...' -ForegroundColor Yellow
    if (!(Test-Path lib)) { New-Item -ItemType Directory lib | Out-Null }
    Invoke-WebRequest 'https://repo1.maven.org/maven2/info/picocli/picocli/4.7.5/picocli-4.7.5.jar' -OutFile $PICOLI_JAR
}

# Compile
Write-Host 'Compiling...' -ForegroundColor Gray
$sources = Get-ChildItem -Path 'src/main/java' -Recurse -Filter '*.java' | ForEach-Object { $_.FullName }
javac -d build -cp 'lib/picocli-4.7.5.jar' $sources

if ($LASTEXITCODE -ne 0) { 
    Write-Host 'Compilation failed' -ForegroundColor Red
    exit 1 
}

# Create JAR
Write-Host 'Packaging JAR...' -ForegroundColor Gray
Set-Content build/MANIFEST.MF "Main-Class: com.revpeek.cli.RevPeekCommand`r`n"
jar -c -m build/MANIFEST.MF -f build/revpeek.jar -C build .

# Shade picocli
Write-Host 'Shading picocli...' -ForegroundColor Gray
$tempDir = 'temp_picocli'
if (Test-Path $tempDir) { Remove-Item -Path $tempDir -Recurse -Force }
New-Item -ItemType Directory -Path $tempDir | Out-Null

Push-Location $tempDir
jar xf ..\lib\picocli-4.7.5.jar
if (Test-Path META-INF) { Remove-Item -Recurse -Force META-INF }
jar -uf ..\build\revpeek.jar .
Pop-Location

Remove-Item -Path $tempDir -Recurse -Force

Write-Host 'Success! Run with: java -jar build/revpeek.jar' -ForegroundColor Green