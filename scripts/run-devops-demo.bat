@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo ======================================================================
echo    NGO PROJECT PROGRESS DASHBOARD - MASTER DEVOPS PIPELINE DEMO
echo ======================================================================
echo.
echo [1/4] COMPILING APPLICATION & RUNNING JUNIT 5 TESTS...
call mvn test -Dtest="!*SeleniumTest"
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Unit test suite failed! Halting pipeline gate.
    exit /b %ERRORLEVEL%
)

echo.
echo [2/4] PACKAGING ARTIFACT (.JAR / .WAR)...
call mvn package -DskipTests
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Packaging failed! Halting pipeline gate.
    exit /b %ERRORLEVEL%
)

echo.
echo [3/4] EXECUTING ANSIBLE IDEMPOTENCY AND ROLLBACK DEMONSTRATION...
python ansible\simulate_ansible.py

echo.
echo ======================================================================
echo [4/4] PIPELINE DEMO COMPLETED SUCCESSFULLY!
echo Artifact generated at: target\ngo-project-dashboard-1.0.0-SNAPSHOT.jar
echo View documentation: docs\FINAL_PROJECT_REPORT.md
echo View slide deck:     docs\PRESENTATION_SLIDES.md
echo View viva answers:   docs\VIVA_PREPARATION_GUIDE.md
echo ======================================================================
