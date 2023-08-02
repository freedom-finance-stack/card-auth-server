#Base image
FROM alpine:3.14

# Docker Image Maintainer Team
MAINTAINER labs-team@razorpay.com
ENV CONTAINER_USER cas-acs
ENV CONTAINER_USERGROUP cas-acs
ENV CURRENT_DIR_PATH scripts/deployment/dockerconf/card-auth-server-acs
ARG CAS_ACS_SERVER_PORT=8080
ENV CARD_AUTH_ACS_SERVER_PORT=$CAS_ACS_SERVER_PORT

# for kubernetes mount configmap volumn to below path and provide acs.yml as configmap
# for docker deployment copy file to below path using -v in run command
# for running java -jar command use --spring.config.additional-location to provide external config file path
ENV EXTERNAL_CONFIG_FILE_PATH /opt/card-auth-server/cas-acs/bin/config/acs.yml

RUN  apk update \
  && apk upgrade \
  && apk add ca-certificates \
  && update-ca-certificates \
  && apk add --update coreutils

#Install Java 11 JDK
RUN wget -O /etc/apk/keys/amazoncorretto.rsa.pub  https://apk.corretto.aws/amazoncorretto.rsa.pub \
    && echo "https://apk.corretto.aws/" >> /etc/apk/repositories \
    && apk update
RUN apk add --update amazon-corretto-11

#Install Supervisord
RUN apk add --update supervisor

# Install Other Libraries
RUN apk add --update curl bash && rm -rf /tmp/* /var/cache/apk/*

# Directories
ENV GLOBAL_LOG_DIRECTORY /var/log
ENV CAS_ACS_LOG_DIRECTORY ${GLOBAL_LOG_DIRECTORY}/cas-acs-logs
ENV CAS_ACS_SUPERVISOR_LOG_DIRECTORY ${GLOBAL_LOG_DIRECTORY}/supervisor
ENV CAS_ACS_BASE_DIRECTORY /opt/card-auth-server/cas-acs
ENV CAS_ACS_BIN_DIRECTORY ${CAS_ACS_BASE_DIRECTORY}/bin

# copying jar to bin
COPY card-auth-server-acs/target/card-auth-server-acs.jar $CAS_ACS_BIN_DIRECTORY/jar/card-auth-server-acs.jar

# Scripts
COPY $CURRENT_DIR_PATH/bin $CAS_ACS_BIN_DIRECTORY
RUN chmod +x -R $CAS_ACS_BIN_DIRECTORY

# Supervisord Setup
COPY $CURRENT_DIR_PATH/supervisor /etc/supervisor/conf.d
COPY $CURRENT_DIR_PATH/supervisor/supervisord.conf /etc/supervisord.conf


#Docker Container HealthChecks
HEALTHCHECK --retries=5 --interval=1m --timeout=10s CMD curl -f http://localhost:$CARD_AUTH_ACS_SERVER_PORT/actuator/health || exit 1
EXPOSE $CARD_AUTH_ACS_SERVER_PORT

WORKDIR $CAS_ACS_LOG_DIRECTORY

# Add non-root user which will be used
RUN addgroup -S $CONTAINER_USERGROUP && adduser -S $CONTAINER_USER -G $CONTAINER_USER
RUN chown -R $CONTAINER_USER:$CONTAINER_USERGROUP $GLOBAL_LOG_DIRECTORY
RUN chown -R $CONTAINER_USER:$CONTAINER_USERGROUP $CAS_ACS_BASE_DIRECTORY
USER $CONTAINER_USER

# Default execution point
ENTRYPOINT ["sh", "-c", "$CAS_ACS_BIN_DIRECTORY/entrypoint.sh"]
