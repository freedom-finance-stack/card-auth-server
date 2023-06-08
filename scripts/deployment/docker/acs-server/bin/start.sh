#!/bin/sh

exec 2>&1

echo "start.sh: Starting Secure Auth ACS Server Service..."
. "$ACS_SERVER_BIN_DIRECTORY"/run_acs_server.sh
ACS_SERVER_SERVICE_PID=$!

echo "ACS_SERVER_SERVICE_PID: " $ACS_SERVER_SERVICE_PID

# Now wait for signals
wait
