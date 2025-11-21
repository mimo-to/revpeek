# bin/revpeek.ps1
param([switch]$Help)
$jar = "$PSScriptRoot\..\build\revpeek.jar"
if (!(Test-Path $jar)) { & "$PSScriptRoot\..\scripts\build.ps1" }
java -jar $jar @args