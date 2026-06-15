@echo off
REM Compila el proyecto usando Maven (requiere Maven instalado)
IF NOT EXIST pom.xml (
  echo pom.xml no encontrado. Ejecuta desde la carpeta del proyecto.
  exit /b 1
)
call mvn -q -DskipTests package
IF %ERRORLEVEL% NEQ 0 (
  echo Error en la compilacion.
  exit /b %ERRORLEVEL%
)

echo Compilacion completa. El jar ejecutable está en target\inventario-1.0.0.jar
