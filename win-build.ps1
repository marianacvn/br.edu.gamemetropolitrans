# Remove previous build output
Write-Host "================================================================================"
Write-Host "Limpando build anterior..."
Write-Host "================================================================================"
Remove-Item -Recurse -Force "metropolitrans-build\out-win"

# Execute Packr to create Windows executable
Write-Host "================================================================================"
Write-Host "Executando Packr..."
Write-Host "================================================================================"
& java -jar "metropolitrans-build\packr-all-4.0.0.jar" `
    --platform windows64 `
    --jdk "metropolitrans-build\windows.zip" `
    --useZgcIfSupportedOs `
    --executable "GameMetropoliTrans" `
    --classpath "lwjgl3\build\libs\MetropoliTrans-1.0.1.jar" `
    --mainclass "br.edu.metropolitrans.lwjgl3.Lwjgl3Launcher" `
    --vmargs "Xmx1G" "XstartOnFirstThread" `
    --resources "files\*" `
    --output "metropolitrans-build\out-win"

# Copy resource files to output directory
Write-Host "================================================================================"
Write-Host "Copiando arquivos de recursos..."
Write-Host "================================================================================"
if (-Not (Test-Path "metropolitrans-build\out-win\files")) {
    New-Item -ItemType Directory -Path "metropolitrans-build\out-win\files"
}
Copy-Item "files\*" -Destination "metropolitrans-build\out-win\files" -Recurse -Force

# Remove all directories in out-win that are also in out-win\files
Write-Host "================================================================================"
Write-Host "Removendo diretórios duplicados..."
Write-Host "================================================================================"
$filesDirs = Get-ChildItem "metropolitrans-build\out-win\files" -Directory
foreach ($dir in $filesDirs) {
    $pathToRemove = "metropolitrans-build\out-win\$($dir.Name)"
    if (Test-Path $pathToRemove) {
        Remove-Item -Recurse -Force $pathToRemove
    }
}

# Copy JAR file to output directory
Write-Host "================================================================================"
Write-Host "Copiando JAR..."
Write-Host "================================================================================"
Copy-Item "lwjgl3\build\libs\MetropoliTrans-1.0.1.jar" -Destination "metropolitrans-build\out-win"

# Compress output directory into a ZIP file using 7zip
Write-Host "================================================================================"
Write-Host "Compactando build..."
Write-Host "================================================================================"
Set-Location "metropolitrans-build\out-win"
& "C:\Program Files\7-Zip\7z.exe" a "..\game-windows.zip" "*"
Set-Location -Path $PSScriptRoot

Write-Host "================================================================================"
Write-Host "Build concluído! O pacote (.zip) está na pasta metropolitrans-build\out-win"
Write-Host "================================================================================"
