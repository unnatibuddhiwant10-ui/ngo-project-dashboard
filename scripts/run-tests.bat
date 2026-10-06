@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo ========================================================
echo Executing JUnit 5 and MockMvc Test Suites
echo ========================================================

mvn test -Dtest="!*SeleniumTest"
