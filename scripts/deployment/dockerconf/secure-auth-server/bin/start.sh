#!/bin/sh

exec 2>&1

start_app() {
  echo "start.sh: Starting Secure Auth ACS Server Service..."
  . "$ACS_SERVER_BIN_DIRECTORY"/run_acs_server.sh
  ACS_SERVER_SERVICE_PID=$!

  echo "ACS_SERVER_SERVICE_PID: " $ACS_SERVER_SERVICE_PID

  # Now wait for signals
  wait "$ACS_SERVER_SERVICE_PID"
}

handle_graceful_shutdown() {
  echo "start.sh: secure-auth-server received SIGTERM. Waiting for secure-auth-server to stop..."
  kill -s SIGTERM "$ACS_SERVER_SERVICE_PID"
  trap - SIGTERM SIGINT
  wait "$ACS_SERVER_SERVICE_PID"
  EXIT_STATUS=$?
  return ${EXIT_STATUS}
}

echo "Graceful Shutdown enabled"
trap handle_graceful_shutdown SIGTERM SIGINT
start_app
