#!/bin/sh

set -e
2>&1

#echo "127.0.0.1 `hostname`" >> /etc/hosts

# Create Directories
mkdir -p $ACS_SERVER_LOG_DIRECTORY
mkdir -p $ACS_SERVER_SUPERVISOR_LOG_DIRECTORY

# Assuming that all mounted directories will be empty on initialisation
# rm -rf ${ACS_SERVER_SUPERVISOR_LOG_DIRECTORY}/*

# Starting supervisord
exec /usr/bin/supervisord -c /etc/supervisor/conf.d/supervisord.conf
