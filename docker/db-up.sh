#!/usr/bin/env bash

echo "Starting oracle-test-db ..."
docker build -t oracle-test-db .
docker-compose up -d
