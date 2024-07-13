@echo off
echo Downloading and updating Docker containers

:: Pull the latest images
docker-compose pull

:: Build any local images and pull the latest base images
docker-compose build --pull

:: Bring up MySQL and Spring Boot application
docker-compose up
