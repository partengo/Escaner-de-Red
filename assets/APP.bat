@echo off
setlocal

echo ======================================
echo  Compilando Escaner de Red
echo ======================================

cd ../

:: Crear carpeta bin si no existe
if not exist bin mkdir bin

:: Compilar todos los .java
javac -d bin src/app/*.java src/ui/*.java src/utils/*.java

if %errorlevel% neq 0 (
    echo Error en la compilacion.
    pause
    exit /b %errorlevel%
)

echo ======================================
echo  Compilacion exitosa - Ejecutando
echo ======================================

java -cp bin app.MainApp

endlocal
pause