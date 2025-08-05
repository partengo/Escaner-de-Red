@echo off
setlocal

echo ======================================
echo  Compilando el proyecto Escáner de Red
echo ======================================

:: Crear carpeta bin si no existe
if not exist bin (
    mkdir bin
)

:: Compilar todos los archivos .java dentro de src/
javac -d bin -sourcepath src src\app\MainApp.java

:: Verificar si la compilación fue exitosa
if %errorlevel% neq 0 (
    echo Error durante la compilación.
    exit /b %errorlevel%
)

echo.
echo ======================================
echo  Compilación exitosa. Ejecutando...
echo ======================================

:: Ejecutar el programa
cd bin
java app.MainApp
cd ..

endlocal
pause