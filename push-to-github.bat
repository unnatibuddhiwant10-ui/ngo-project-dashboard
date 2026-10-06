@echo off
title Push NGO Project Dashboard to GitHub
color 0a

echo ======================================================================
echo       PUSH NGO PROJECT PROGRESS DASHBOARD TO GITHUB
echo ======================================================================
echo.
echo Target Repository:
echo   https://github.com/unnatibuddhiwant10-ui/ngo-project-progress-dashboard.git
echo.
echo Pushing local branch 'main' to GitHub...
echo (If prompted, please sign in via your browser)
echo.

git push -u origin main

echo.
if %ERRORLEVEL% equ 0 (
    echo ======================================================================
    echo SUCCESS! Project has been successfully pushed to GitHub.
    echo View online: https://github.com/unnatibuddhiwant10-ui/ngo-project-progress-dashboard
    echo ======================================================================
) else (
    echo [ERROR] Git push failed. Please check your network or credentials.
)

pause
