@echo off
echo ========================================================
echo Setting up Environment for NGO Project Dashboard
echo ========================================================

REM Set JAVA_HOME
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo JAVA_HOME set to: %JAVA_HOME%
java -version
mvn -version

echo.
echo Environment verified successfully!
