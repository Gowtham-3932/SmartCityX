@echo off
setlocal enabledelayedexpansion

REM Attempt to locate java
set "JAVA_CMD="
if exist "C:\Users\Gowtham\AppData\Local\Programs\STM32CubeMX\jre\bin\java.exe" (
    set "JAVA_CMD=C:\Users\Gowtham\AppData\Local\Programs\STM32CubeMX\jre\bin\java.exe"
) else if exist "C:\Users\Gowtham\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\java.exe" (
    set "JAVA_CMD=C:\Users\Gowtham\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.11.v20260515-1531\jre\bin\java.exe"
) else if defined JAVA_HOME (
    if exist "%JAVA_HOME%\bin\java.exe" set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
) else (
    where java >nul 2>&1
    if %errorlevel% equ 0 set "JAVA_CMD=java"
)

if "!JAVA_CMD!"=="" (
    echo [ERROR] java runtime not found.
    exit /b 1
)

if not exist bin\smartcityx\SmartCityX.class (
    echo bin/ directory not found or uncompiled. Running build.bat first...
    call build.bat
    if %errorlevel% neq 0 exit /b %errorlevel%
)

"%JAVA_CMD%" -cp bin smartcityx.SmartCityX %*
