#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -lt 2 ]; then
  echo "Usage: $0 <dockerhub_user> <tag>"
  exit 1
fi

DOCKERHUB_USER="$1"
TAG="$2"
REF="${DOCKERHUB_USER}/powergym-compose:${TAG}"

if docker compose publish --help >/dev/null 2>&1; then
  echo "Publishing Compose OCI artifact ${REF}..."
  docker compose -f docker-compose.yml publish --with-env "${REF}"
  echo "Done: ${REF}"
else
  echo "Your Docker Compose version does not include 'docker compose publish'."
  echo "Upgrade Docker/Compose and run:"
  echo "  docker compose -f docker-compose.yml publish ${REF}"
  exit 1
fi
