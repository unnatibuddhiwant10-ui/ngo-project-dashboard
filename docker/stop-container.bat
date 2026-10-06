@echo off
echo Stopping and removing NGO Project Dashboard Container...
docker stop ngo-dashboard-app
docker rm ngo-dashboard-app
echo Container successfully stopped and cleaned.
