@echo off
setlocal enabledelayedexpansion

echo ====================================================================
echo                   SmartCityX Build Script
echo ====================================================================

REM Attempt to locate javac
set "JAVAC_CMD="
where javac >nul 2>&1
if %errorlevel% equ 0 set "JAVAC_CMD=javac"

if "!JAVAC_CMD!"=="" (
    if exist "C:\Users\Gowtham\AppData\Local\Programs\STM32CubeMX\jre\bin\javac.exe" (
        set "JAVAC_CMD=C:\Users\Gowtham\AppData\Local\Programs\STM32CubeMX\jre\bin\javac.exe"
    ) else if exist "C:\Users\Gowtham\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\javac.exe" (
        set "JAVAC_CMD=C:\Users\Gowtham\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\javac.exe"
    ) else if defined JAVA_HOME (
        if exist "%JAVA_HOME%\bin\javac.exe" set "JAVAC_CMD=%JAVA_HOME%\bin\javac.exe"
    )
)

if "!JAVAC_CMD!"=="" (
    echo [ERROR] javac compiler not found on PATH or standard directory.
    exit /b 1
)

if not exist bin mkdir bin

echo Compiling Java source files using: "!JAVAC_CMD!"
"%JAVAC_CMD%" -d bin src\smartcityx\*.java
set BUILD_STATUS=%errorlevel%

if %BUILD_STATUS% equ 0 (
    echo [SUCCESS] SmartCityX compiled cleanly to bin/
) else (
    echo [ERROR] Compilation failed.
    exit /b %BUILD_STATUS%
)
