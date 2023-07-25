#!/bin/sh

set -e
2>&1

#echo "127.0.0.1 `hostname`" >> /etc/hosts

# Create Directories
mkdir -p "$CAS_ACS_LOG_DIRECTORY"
mkdir -p "$CAS_ACS_SUPERVISOR_LOG_DIRECTORY"

# Assuming that all mounted directories will be empty on initialisation
# rm -rf ${CAS_ACS_SUPERVISOR_LOG_DIRECTORY}/*

# Starting supervisord
exec /usr/bin/supervisord -c /etc/supervisor/conf.d/supervisord.conf
