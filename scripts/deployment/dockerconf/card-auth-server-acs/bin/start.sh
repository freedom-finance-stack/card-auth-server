#!/bin/sh

exec 2>&1

start_app() {
  echo "start.sh: Starting Card Auth Server ACS Service..."
  . "$CAS_ACS_BIN_DIRECTORY"/run_cas_acs.sh
  CAS_ACS_SERVICE_PID=$!

  echo "CAS_ACS_SERVICE_PID: " $CAS_ACS_SERVICE_PID

  # Now wait for signals
  wait "$CAS_ACS_SERVICE_PID"
}

handle_graceful_shutdown() {
  echo "start.sh: card-auth-server-acs received SIGTERM. Waiting for card-auth-server-acs to stop..."
  kill -s SIGTERM "$CAS_ACS_SERVICE_PID"
  trap - SIGTERM SIGINT
  wait "$CAS_ACS_SERVICE_PID"
  EXIT_STATUS=$?
  return ${EXIT_STATUS}
}

echo "Graceful Shutdown enabled"
trap handle_graceful_shutdown SIGTERM SIGINT
start_app
