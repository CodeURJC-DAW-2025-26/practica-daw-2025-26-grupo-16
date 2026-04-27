#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -lt 2 ]; then
  echo "Usage: $0 <dockerhub_user> <tag>"
  exit 1
fi

DOCKERHUB_USER="$1"
TAG="$2"
IMAGE="${DOCKERHUB_USER}/powergym:${TAG}"
ROOT_DIR="$(cd "$(dirname "$0")/../.." && pwd)"

echo "Building image ${IMAGE}..."
docker build -t "${IMAGE}" -f backend/Dockerfile "${ROOT_DIR}"

echo "Pushing image ${IMAGE}..."
docker push "${IMAGE}"

echo "Done: ${IMAGE}"
