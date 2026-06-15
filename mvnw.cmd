@echo off
rem Apache Maven Wrapper (minimal)
setlocal
set SCRIPT_DIR=%~dp0
set WRAPPER_DIR=%SCRIPT_DIR%\.mvn\wrapper
set WRAPPER_JAR=%WRAPPER_DIR%\maven-wrapper.jar
if not exist "%WRAPPER_JAR%" (
  echo Missing %WRAPPER_JAR%
  echo You can run "mvn -N io.takari:maven:wrapper" to generate the wrapper, or ask me to add it.
  exit /b 1
)
java -jar "%WRAPPER_JAR%" %*
endlocal
@echo off
REM Simple mvnw shim for Windows: uses mvn if installed, otherwise shows instructions.
where mvn >nul 2>&1
if %ERRORLEVEL%==0 (
  mvn %*
) else (
  echo Maven no está instalado. Para generar el Maven Wrapper ejecuta:
  echo mvn -N io.takari:maven:wrapper
  echo O instala Maven o pide que agregue el wrapper completo.
  exit /b 1
)
