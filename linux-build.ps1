# Remove previous build output
Write-Host "================================================================================"
Write-Host "Limpando build anterior..."
Write-Host "================================================================================"
Remove-Item -Recurse -Force "metropolitrans-build\out-linux"

# Execute Packr to create Linux executable
Write-Host "================================================================================"
Write-Host "Executando Packr..."
Write-Host "================================================================================"
& java -jar "metropolitrans-build\packr-all-4.0.0.jar" `
    --platform linux64 `
    --jdk "metropolitrans-build\linux.tar.gz" `
    --useZgcIfSupportedOs `
    --executable "GameMetropoliTrans" `
    --classpath "lwjgl3\build\libs\MetropoliTrans-1.0.0.jar" `
    --mainclass "br.edu.metropolitrans.lwjgl3.Lwjgl3Launcher" `
    --vmargs "Xmx1G" "XstartOnFirstThread" `
    --resources "files\*" `
    --icon "files/icons/icon-house.png" `
    --output "metropolitrans-build\out-linux"

# Copy resource files to output directory
Write-Host "================================================================================"
Write-Host "Copiando arquivos de recursos..."
Write-Host "================================================================================"
if (-Not (Test-Path "metropolitrans-build\out-linux\files")) {
    New-Item -ItemType Directory -Path "metropolitrans-build\out-linux\files"
}
Copy-Item "files\*" -Destination "metropolitrans-build\out-linux\files" -Recurse -Force

# Remove all directories in out-linux that are also in out-linux\files
Write-Host "================================================================================"
Write-Host "Removendo diretórios duplicados..."
Write-Host "================================================================================"
$filesDirs = Get-ChildItem "metropolitrans-build\out-linux\files" -Directory
foreach ($dir in $filesDirs) {
    $pathToRemove = "metropolitrans-build\out-linux\$($dir.Name)"
    if (Test-Path $pathToRemove) {
        Remove-Item -Recurse -Force $pathToRemove
    }
}

# Copy JAR file to output directory
Write-Host "================================================================================"
Write-Host "Copiando JAR..."
Write-Host "================================================================================"
Copy-Item "lwjgl3\build\libs\MetropoliTrans-1.0.0.jar" -Destination "metropolitrans-build\out-linux"

# Compress output directory into a ZIP file using 7zip
Write-Host "================================================================================"
Write-Host "Compactando build..."
Write-Host "================================================================================"
Set-Location "metropolitrans-build\out-linux"
& "C:\Program Files\7-Zip\7z.exe" a "..\game-linux.zip" "*"
Set-Location -Path $PSScriptRoot

Write-Host "================================================================================"
Write-Host "Build concluído! O pacote (.zip) está na pasta metropolitrans-build\out-linux"
Write-Host "================================================================================"
