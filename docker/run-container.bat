@echo off
echo ========================================================
echo Building and Running NGO Project Dashboard Container
echo ========================================================

REM Build the image
docker build -t ngo-project-dashboard:latest -f docker/Dockerfile .

REM Stop any existing container
docker stop ngo-dashboard-app 2>nul
docker rm ngo-dashboard-app 2>nul

REM Run the container
docker run -d --name ngo-dashboard-app -p 8080:8080 ngo-project-dashboard:latest

echo.
echo Container started successfully!
echo Checking status:
docker ps --filter "name=ngo-dashboard-app"
echo.
echo View application at: http://localhost:8080/
echo Check health at: http://localhost:8080/health
