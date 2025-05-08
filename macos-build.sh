#!/bin/bash

# Remove previous build output
echo "================================================================================"
echo "Limpando build anterior..."
echo "================================================================================"
rm -rf "metropolitrans-build/out-macos"

# Extrair o JDK do arquivo tar.gz
echo "================================================================================"
echo "Extraindo JDK..."
echo "================================================================================"
if [ -f "metropolitrans-build/macos.tar.gz" ]; then
    tar -xzf "metropolitrans-build/macos.tar.gz" -C "metropolitrans-build/"
else
    echo "Erro: O arquivo metropolitrans-build/macos.tar.gz não foi encontrado!"
    exit 1
fi

# Execute Packr to create macOS executable
echo "================================================================================"
echo "Executando Packr..."
echo "================================================================================"
java -jar "metropolitrans-build/packr-all-4.0.0.jar" \
    --platform mac \
    --jdk "metropolitrans-build/macos" \
    --useZgcIfSupportedOs \
    --executable "GameMetropoliTrans" \
    --classpath "lwjgl3/build/libs/MetropoliTrans-1.0.0.jar" \
    --mainclass "br.edu.metropolitrans.lwjgl3.Lwjgl3Launcher" \
    --vmargs "Xmx1G" "XstartOnFirstThread" \
    --resources "files/*" \
    --icon "files/icons/icon-house.png" \
    --output "metropolitrans-build/out-macos"

# Copy resource files to output directory
echo "================================================================================"
echo "Copiando arquivos de recursos..."
echo "================================================================================"
mkdir -p "metropolitrans-build/out-macos/files"
cp -R "files/"* "metropolitrans-build/out-macos/files"

# Remove all directories in out-macos that are also in out-macos/files
echo "================================================================================"
echo "Removendo diretórios duplicados..."
echo "================================================================================"
for dir in metropolitrans-build/out-macos/files/*; do
    if [ -d "$dir" ]; then
        pathToRemove="metropolitrans-build/out-macos/$(basename "$dir")"
        if [ -d "$pathToRemove" ]; then
            rm -rf "$pathToRemove"
        fi
    fi
done

# Copy JAR file to output directory
echo "================================================================================"
echo "Copiando JAR..."
echo "================================================================================"
cp "lwjgl3/build/libs/MetropoliTrans-1.0.0.jar" "metropolitrans-build/out-macos"

# Compress output directory into a TAR.GZ file
echo "================================================================================"
echo "Compactando build..."
echo "================================================================================"
cd "metropolitrans-build/out-macos" || exit
tar -czf "../game-macos.tar.gz" *
cd - || exit

echo "================================================================================"
echo "Build concluído! O pacote (.tar.gz) está na pasta metropolitrans-build/out-macos"
echo "================================================================================"