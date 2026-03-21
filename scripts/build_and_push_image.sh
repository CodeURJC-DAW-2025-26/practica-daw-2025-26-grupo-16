#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -lt 2 ]; then
  echo "Usage: $0 <dockerhub_user> <tag>"
  exit 1
fi

DOCKERHUB_USER="$1"
TAG="$2"
IMAGE="${DOCKERHUB_USER}/powergym:${TAG}"

echo "Building image ${IMAGE}..."
docker build -t "${IMAGE}" -f backend/Dockerfile backend

echo "Pushing image ${IMAGE}..."
docker push "${IMAGE}"

echo "Done: ${IMAGE}"
