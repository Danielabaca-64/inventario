@echo off
REM Ejecuta el jar generado por Maven
IF NOT EXIST target\inventario-1.0.0.jar (
  echo Jar no encontrado. Ejecuta build.bat primero.
  exit /b 1
)
java -jar target\inventario-1.0.0.jar
