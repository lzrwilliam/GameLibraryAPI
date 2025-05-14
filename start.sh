#!/bin/bash
set -x

mkdir -p /workspaces/jenkins_config

# Se

# Rulează cu variabila setată
IMAGE_TAG=$IMAGE_TAG docker compose --profile mongo --profile hello-service up -d
