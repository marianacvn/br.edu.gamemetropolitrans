@echo off
rmdir /s /q metropolitrans-build\out-win

java -jar metropolitrans-build\packr-all-4.0.0.jar ^
     --platform windows64 ^
     --jdk metropolitrans-build\windows.zip ^
     --useZgcIfSupportedOs ^
     --executable GameMetropoliTrans ^
     --classpath lwjgl3\build\libs\MetropoliTrans-1.0.0.jar ^
     --mainclass br.edu.metropolitrans.lwjgl3.Lwjgl3Launcher ^
     --vmargs Xmx1G XstartOnFirstThread ^
     --resources files\* ^
     --output metropolitrans-build\out-win

echo.
echo ============================
echo 🚀 Build concluído! O executável está na pasta metropolitrans-build\out-win
echo ============================
