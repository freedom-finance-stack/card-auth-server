#!/bin/sh

set -e
2>&1

# Constants
JAVA_EXEC=/usr/bin/java
JAVA_EXTRA_OPTS=""
JAVA_GC_OPTS=""
REMOTE_DEBUGGING_PORT=7081

if [ "$ENVIRONMENT" = "prod" ]; then
  JAVA_GC_OPTS="$JAVA_GC_OPTS -Xms10G -Xmx10G -XX:+UseG1GC"
else
  # Enabling remote debugging only in non-prod environment
  echo "Enabling remote debugging."
  JAVA_EXTRA_OPTS="$JAVA_EXTRA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${REMOTE_DEBUGGING_PORT}"

  JAVA_GC_OPTS="$JAVA_GC_OPTS -Xms2G -Xmx2G -XX:+UseG1GC"
fi

# todo - Add spring.active.profiles environment variable in Java Opts.
JAVA_OPTS="$JAVA_EXTRA_OPTS $JAVA_GC_OPTS -Djava.net.preferIPv4Stack=true
          -Dsun.jnu.encoding=UTF-8
          -Dfile.encoding=UTF-8
          -Duser.timezone=UTC
          --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED"

# Starting Secure Auth ACS Server Service
echo "Starting Secure Auth ACS Server Service..."
echo "JAVA_OPTS: " $JAVA_OPTS
exec $JAVA_EXEC $JAVA_OPTS -jar ${ACS_SERVER_BIN_DIRECTORY}/jar/secure-auth-acs-server.jar &
echo "Started Secure Auth ACS Server Service..."
