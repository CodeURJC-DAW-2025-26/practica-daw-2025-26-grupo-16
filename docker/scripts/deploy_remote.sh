#!/usr/bin/env bash
set -euo pipefail

if [ "$#" -lt 4 ]; then
  echo "Usage: $0 <user@host> <dockerhub_user> <tag> <mode>"
  echo "mode: seeded | clean"
  exit 1
fi

REMOTE="$1"
DOCKERHUB_USER="$2"
TAG="$3"
MODE="$4"
OCI_REF="docker.io/${DOCKERHUB_USER}/powergym-compose:${TAG}"

if [ "${MODE}" = "seeded" ]; then
  DDL_AUTO="create"
elif [ "${MODE}" = "clean" ]; then
  DDL_AUTO="none"
else
  echo "Invalid mode '${MODE}'. Use seeded or clean."
  exit 1
fi

echo "Deploying on ${REMOTE} using ${OCI_REF} (${MODE})..."
ssh "${REMOTE}" "DDL_AUTO=${DDL_AUTO} docker compose -f oci://${OCI_REF} up -d"

echo "Deployment command sent."
