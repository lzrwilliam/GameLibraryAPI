#!/bin/bash
set -x

mkdir -p /workspaces/jenkins_config

# Dacă IMAGE_TAG nu este setat, folosim latest
export IMAGE_TAG=${IMAGE_TAG:-latest}

docker compose --profile mongo --profile hello-service up -d