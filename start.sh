#!/bin/bash

# Pull the latest images
docker compose pull

TODO: try to run this new command. You should get an error with this GET request: http://127.0.0.1:8080/api/accounts?iban=IT10474608000005006107XXXXX
# Build any local images and pull the latest base images
docker compose build --pull --no-cache

# Bring up MySQL and Spring Boot application
docker compose up
