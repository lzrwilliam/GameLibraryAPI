#!/bin/bash -e

# Dacă rulezi:
# ./build.sh face build cu: 0.0.1-SNAPSHOT

#Dacă rulezi: APP_VERSION=1.5.0 ./build.sh face build cu: 1.5.0


APP_VERSION=${APP_VERSION:-0.0.1-SNAPSHOT}

./gradlew clean test build -PappVersion=$APP_VERSION