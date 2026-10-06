@echo off
set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

set "PORT=8085"

echo ========================================================
echo Starting NGO Project Progress Dashboard (Spring Boot)
echo ========================================================
echo Dashboard will be available at: http://localhost:%PORT%/
echo H2 DB Console available at:     http://localhost:%PORT%/h2-console
echo Health Endpoint available at:   http://localhost:%PORT%/health
echo ========================================================

java -jar target\ngo-project-dashboard-1.0.0-SNAPSHOT.jar
