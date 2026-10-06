@echo off
title NGO Project Progress Dashboard - Launcher
color 0b

echo ======================================================================
echo          NGO PROJECT PROGRESS DASHBOARD - 1-CLICK LAUNCHER
echo ======================================================================
echo.

REM Set Java 21 environment
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo [1/3] Verifying Java Environment...
java -version 2>nul
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java 17/21 was not found! Please ensure JDK is installed.
    pause
    exit /b 1
)

REM Use port 8085 to avoid conflicts
set "PORT=8085"

echo.
echo [2/3] Starting Spring Boot Application on Port %PORT%...
echo ----------------------------------------------------------------------
echo  * Web Application URL : http://localhost:%PORT%/
echo  * Risk Alerts Center  : http://localhost:%PORT%/alerts
echo  * H2 Database Console : http://localhost:%PORT%/h2-console
echo  * Health Check Status : http://localhost:%PORT%/health
echo ----------------------------------------------------------------------
echo.
echo [3/3] Opening browser in 4 seconds...
start "" timeout /t 4 /nobreak >nul & start http://localhost:%PORT%/

echo.
echo ======================================================================
echo APPLICATION IS RUNNING! (Press Ctrl+C in this window to stop server)
echo ======================================================================
echo.

java -jar target\ngo-project-dashboard-1.0.0-SNAPSHOT.jar
pause
