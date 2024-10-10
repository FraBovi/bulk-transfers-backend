#!/bin/bash

# Pull the latest images
docker compose pull

# Build any local images and pull the latest base images
docker compose build --pull --no-cache

# Bring up MySQL and Spring Boot application
docker compose up
