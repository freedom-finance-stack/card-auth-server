#Base image
FROM alpine:3.14

# Docker Image Maintainer Team
MAINTAINER labs-team@razorpay.com

#Update repository
RUN  apk update \
  && apk upgrade \
  && apk add ca-certificates \
  && update-ca-certificates \
  && apk add --update coreutils && rm -rf /var/cache/apk/*

#Install Java 11 JDK
RUN wget -O /etc/apk/keys/amazoncorretto.rsa.pub  https://apk.corretto.aws/amazoncorretto.rsa.pub \
    && echo "https://apk.corretto.aws/" >> /etc/apk/repositories \
    && apk update
RUN apk add --update amazon-corretto-11

#Install Supervisord
RUN apk add --update supervisor && rm  -rf /tmp/* /var/cache/apk/*

# Install Other Libraries
RUN apk add --update curl unzip bash

# Directories
ENV GLOBAL_LOG_DIRECTORY /var/log
ENV ACS_SERVER_LOG_DIRECTORY ${GLOBAL_LOG_DIRECTORY}/acs-server-logs
ENV ACS_SERVER_SUPERVISOR_LOG_DIRECTORY ${GLOBAL_LOG_DIRECTORY}/supervisor
ENV ACS_SERVER_BASE_DIRECTORY /opt/secure-auth/acs-server
ENV ACS_SERVER_BIN_DIRECTORY ${ACS_SERVER_BASE_DIRECTORY}/bin

WORKDIR $ACS_SERVER_LOG_DIRECTORY

# copying jar to bin
COPY secure-auth-acs-server/target/secure-auth-acs-server.jar $ACS_SERVER_BIN_DIRECTORY/jar/secure-auth-acs-server.jar

# Scripts
COPY scripts/deployment/docker/acs-server/bin $ACS_SERVER_BIN_DIRECTORY
RUN chmod +x -R $ACS_SERVER_BIN_DIRECTORY

# Supervisord Setup
COPY scripts/deployment/docker/acs-server/supervisor /etc/supervisor/conf.d
COPY scripts/deployment/docker/acs-server/supervisor/supervisord.conf /etc/supervisord.conf

#Docker Container HealthChecks
ENV HEALTH_CHECK_URI=/actuator/healthcheck
EXPOSE 7070
HEALTHCHECK --retries=5 --interval=1m --timeout=10s CMD curl -f http://localhost:7070/actuator/health || exit 1

# Default execution point
ENTRYPOINT ["sh", "-c", "$ACS_SERVER_BIN_DIRECTORY/entrypoint.sh"]
