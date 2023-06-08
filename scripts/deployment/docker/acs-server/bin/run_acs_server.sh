#!/bin/sh

set -e
2>&1

# Constants
JAVA_EXEC=/usr/bin/java

JAVA_OPTS="-Djava.net.preferIPv4Stack=true
          -Dsun.jnu.encoding=UTF-8
          -Dfile.encoding=UTF-8
          -Duser.timezone=UTC
          --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED"

# Starting Secure Auth ACS Server Service
echo "Starting Secure Auth ACS Server Service..."
echo "JAVA_OPTS: " $JAVA_OPTS
exec $JAVA_EXEC $JAVA_OPTS -jar ${ACS_SERVER_BIN_DIRECTORY}/jar/secure-auth-acs-server.jar &
echo "Started Secure Auth ACS Server Service..."