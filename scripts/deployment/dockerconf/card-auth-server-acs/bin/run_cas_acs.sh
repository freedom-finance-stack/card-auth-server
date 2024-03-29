#!/bin/sh

set -e
2>&1

# Constants
JAVA_EXEC=/usr/bin/java
JAVA_EXTRA_OPTS=""
JAVA_GC_OPTS=""
REMOTE_DEBUGGING_PORT=7081

if [ "$SPRING_PROFILES_ACTIVE" = "prod" ]; then
  JAVA_GC_OPTS="$JAVA_GC_OPTS -Xms2G -Xmx2G -XX:+UseG1GC"
else
  # Enabling remote debugging only in non-prod environment
  echo "Enabling remote debugging."
  JAVA_EXTRA_OPTS="$JAVA_EXTRA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${REMOTE_DEBUGGING_PORT}"

  JAVA_GC_OPTS="$JAVA_GC_OPTS -Xms2G -Xmx2G -XX:+UseG1GC"
fi

JAVA_OPTS="$JAVA_EXTRA_OPTS $JAVA_GC_OPTS -Djava.net.preferIPv4Stack=true
          -Dsun.jnu.encoding=UTF-8
          -Dfile.encoding=UTF-8
          -Duser.timezone=UTC
          --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED
          --add-opens java.base/java.time=ALL-UNNAMED"

# Starting Card Auth Server ACS Service
echo "Starting Card Auth Server ACS Service..."
echo "JAVA_OPTS: " "$JAVA_OPTS"
exec $JAVA_EXEC $JAVA_OPTS -jar "${CAS_ACS_BIN_DIRECTORY}/jar/card-auth-server-acs.jar" "--spring.config.additional-location=optional:${EXTERNAL_CONFIG_FILE_PATH}" &
echo "Started Card Auth Server ACS Service..."
