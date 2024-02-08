# Card Auth Server (ACS)

The Card Auth Server (ACS) is a powerful and flexible server that provides authentication services for card
transactions. This README provides instructions on how to deploy the ACS server using various methods and how to provide
the `acs.yml` configuration file to customize the server's behavior and settings.

## Deployment Options

The ACS server can be deployed using the following methods:

1. Running the JAR Command
2. Docker Run
3. Docker Compose
4. Kubernetes
5. Using Built-In Automated Shell Script

For each deployment option, you can provide the `acs.yml` configuration file to customize the server's behavior and
settings. find out more acs.yml [here](#acs-configuration-acsyml-explained)

NOTE: File name has to be acs.yml

### 1. Running the JAR Command

To run the ACS server using the JAR command, use the following command:

```bash
java -jar card-auth-server-acs.jar --spring.config.additional-location=/path/to/acs.yml
```

Replace /path/to/acs.yml with the path to your custom acs.yml configuration file. This command will start the ACS server
and load the configuration from the provided acs.yml file.

### 2. Docker Run

To deploy the ACS server using Docker, you can copy the acs.yml configuration file to a specific path inside the
container using the -v (volume) option. Here's an example command:

```bash
docker run -v /path/to/acs.yml:/opt/card-auth-server/cas-acs/bin/config/acs.yml card-auth-server-acs:latest
```

Replace /path/to/acs.yml with the path to your custom acs.yml configuration file on the host machine and
card-auth-server-acs:latest with the name of your ACS Docker image.

### 3. Docker Compose

To use Docker Compose for deploying the ACS server, you can set environment variables in the docker-compose-dev.yaml
file. Docker Compose dev is
located [here](https://github.com/razorpay/card-auth-server/blob/master/scripts/deployment/dockerconf/card-auth-server-acs/docker-compose-dev.yaml).
make sure to run following command from root directory of project.

~Here's an example:

```bash
 EXTERNAL_YAML_PATH=/path/to/acs.yml docker-compose -f ./scripts/deployment/dockerconf/card-auth-server-acs/docker-compose-dev.yaml up -d
 ```

### 4. Kubernetes

For Kubernetes deployment, you can create a ConfigMap to store the acs.yml configuration file and mount it to a specific
path in the ACS container. Here's an example of how to create the ConfigMap:

```bash
kubectl create configmap acs-config --from-file=/path/to/acs.yml
```

Then, in your Kubernetes deployment YAML file, use the following:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: acs-deployment
spec:
  template:
    spec:
      volumes:
        - name: config-volume
          configMap:
            name: acs-config
      containers:
        - name: acs-container
          image: card-auth-server-acs
          volumeMounts:
            - name: config-volume
              mountPath: /opt/card-auth-server/cas-acs/bin/config/acs.yml
              subPath: acs.yml

```

Prefix card-auth-server-acs with the name of your ACS Docker image.

### 5. Using Built-In Automated Shell Script

* Go to Card Auth Server Repo directory
    ``` 
    cd card-auth-server
    ```
* Run below command
    ```
    sh ./scripts/deployment/dev/cas-acs-dev-deployment.sh
    ```

## ACS Configuration (acs.yml) Explained

Below is the explanation of the attributes present in the acs.yml configuration file:

Sample acs.yml is located [here](../config/external/sample-acs.yml). File name has to be acs.yml

### ACS (Card Auth Server) configuration.

The ACS (Access Control Server) configuration allows you to customize various settings for the Access Control Server.
Below are the available configuration options:

#### Example Configuration

```yaml
acs:
  referenceNumber: 99995678901234567890
  operatorId:
    visa: 123456789012345
    mastercard: 123456789012345
    amex: 123456789012345
```

- `acs.referenceNumber`: A unique reference number used for identification purposes within the ACS.

- `acs.operatorId`: An object that defines the operator IDs for different card networks.
    - `visa`: The operator ID for Visa card network.
    - `mastercard`: The operator ID for Mastercard card network.
    - `amex`: The operator ID for American Express card network.

### Test Configuration

The `test` section allows you to configure test-related settings, which will help you run service in non prod env easily.
Example Configuration
```yaml
test:
  enable-decryption-encryption: true
  enable : false
```

* `test.enable-decryption-encryption`: 
  * A boolean flag indicating to avoid encryption and decryption for challenge flow to run service in lower env.
  * Make sure it is always set to true in production.


* `test.enable`: 
  * A boolean flag indicating whether testing is enabled, it can be set to false in case of local testing.
  * Always keep this flag set to false in prod.

#### Notification Configuration
The notification section configures notification settings, including SMS and email channels.

Example Configuration
```yaml
notification:
  sms:
    enabledChannel: "dummy-sms-server"
  email:
    enabledChannel: "dummy-email-server"
    simpleSMTP:
      host: your-smtp-server.com
      port: 587
```
* `notification.sms.enabledChannel`: 

  * Defines the SMS channel to be used for sending notification such as OTP, It has to be one a value mentioned in [SMS channel type](https://github.com/freedom-finance-stack/card-auth-server-extensions/blob/master/src/main/java/org/freedomfinancestack/extensions/notification/enums/SMSChannelType.java) in extension repo. 
  * Currently, we only have mock implementation. Make sure to implement [SMS notification service interface](https://github.com/freedom-finance-stack/card-auth-server-extensions/blob/master/src/main/java/org/freedomfinancestack/extensions/notification/SMSNotificationService.java) and configure it in [SMS factory](https://github.com/freedom-finance-stack/card-auth-server-extensions/blob/master/src/main/java/org/freedomfinancestack/extensions/notification/factory/SMSNotificationFactory.java) method


* `notification.email.enabledChannel`: 
  * Defines the Email channel to be used for sending notification such as OTP, It has to be one a value mentioned in [email channel type](https://github.com/freedom-finance-stack/card-auth-server-extensions/blob/master/src/main/java/org/freedomfinancestack/extensions/notification/enums/EmailChannelType.java) in extension repo. 
  * Currently, we only have mock implementation. Make sure to implement [email notification service interface](https://github.com/freedom-finance-stack/card-auth-server-extensions/blob/master/src/main/java/org/freedomfinancestack/extensions/notification/EmailNotificationService.java) and configure it in [Email factory](https://github.com/freedom-finance-stack/card-auth-server-extensions/blob/master/src/main/java/org/freedomfinancestack/extensions/notification/factory/EmailNotificationFactory.java) method


* `notification.email.simpleSMTP`: 
    *  this was added as placeholder to explain that we can add more configuration here to support our active notification channels. Right we only have mock implementation, you need to implement as per your use case
  
> You need to specify the Qualified bean for Channel providing SMS and Email notification for OTP which can be found in [card-auth-server-extensions](https://github.com/freedom-finance-stack/card-auth-server-extensions/blob/master/src/main/java/org/freedomfinancestack/extensions/notification/NotificationConfiguration.java).  

### OTP (One-Time Password) Configuration
The otp section configures settings related to one-time passwords for authentication.

Example Configuration
```yaml
otp:
  sms:
    templateName: acs.sms.otp.sample
  email:
    from: acs@bank.com
    templateName: acs.email.otp.sample
    subjectText: "Verification code"
```
* `otp.sms.templateName`: The template name for SMS one-time passwords.
* `otp.email.from`: The email address from which one-time password emails will be sent.
* `otp.email.templateName`: The template name for email one-time passwords.
* `otp.email.subjectText`: The subject text for email one-time passwords.

### Gateway Configuration
The gateway section encompasses configurations for various gateway services utilized within the ACS, such as DS Connection configuration and Three DS requester server connection configuration.

Example Configuration
```yaml
gateway:
  services:
    VISA_DS:
      mock: true
      url: https://sample-ds.com
      useSSL: false
      connectTimeout: 5000
      responseTimeout: 5000
      keyStore:
        path: ${VISA_DS_KEYSTORE_PATH:}
        password: ${VISA_DS_KEYSTORE_PASSWORD:}
      trustStore:
        path: ${VISA_DS_TRUSTSTORE_PATH:}
        password: ${VISA_DS_TRUSTSTORE_PASSWORD:changeit}
      retryable:
        maxAttempts: 2
        backOffPeriod: 1000   #ms
```


* `mock`:
  * **Description**: Specifies whether the service should operate in mock mode.
  * **Value**: `true` if mock mode is enabled; `false` otherwise.
  * **Purpose**: When set to `true`, the service operates in a simulated/mock mode, useful for testing and development scenarios.


* `url`:
  * **Description**: The URL of the service.
  * **Value**: A valid URL, e.g., https://sample-ds.com.
  * **Purpose**: Defines the endpoint URL where the ACS communicates with the service for processing card transactions.


* `useSSL`:
  * **Description**: Specifies whether SSL (Secure Socket Layer) should be used for communication.
  * **Value**: true if SSL is enabled; false otherwise.
  * **Purpose**: Determines whether the communication with the service is encrypted using SSL. 


* `connectTimeout`:
  * **Description**: The maximum time (in milliseconds) the ACS will wait for a connection to be established with the service.
  * **Value**: An integer representing the timeout duration, e.g., 5000 for 5 seconds.
  * **Purpose**: Sets the timeout for establishing a connection with the service.


* `responseTimeout:`
  * **Description**: The maximum time (in milliseconds) the ACS will wait for a response from the service.
  * **Value**: An integer representing the timeout duration, e.g., 5000 for 5 seconds.
  * **Purpose**: Sets the timeout for receiving a response from the service.


* `keyStore`:
  * **Description**: Configures the keystore properties for secure communication.
  * **Properties**:
    * **path**: The path to the keystore file. It can be specified using environment variables, e.g., ${<SERVICE_NAME>_KEYSTORE_PATH:}.
    * **password**: The password for accessing the keystore. It can be specified using environment variables, e.g., ${<SERVICE_NAME>_KEYSTORE_PASSWORD:}.
  * **Purpose**: Ensures secure communication by providing the path and password for the keystore.


* `trustStore`:
  * **Description**: Configures the truststore properties for secure communication.
  * **Properties**:
    * **path**: The path to the truststore file. It can be specified using environment variables, e.g., ${<SERVICE_NAME>_TRUSTSTORE_PATH:}.
    * **password**: The password for accessing the truststore. It can be specified using environment variables, e.g., ${<SERVICE_NAME>_TRUSTSTORE_PASSWORD:<PASSWORD>}.
  * **Purpose**: Ensures secure communication by providing the path and password for the truststore.
  

* `retryable`:
  * **Description**: Configures retry settings in case of failures during communication.
  * **Properties**:
    * **maxAttempts**: The maximum number of retry attempts.
    * **backOffPeriod**: The time (in milliseconds) to wait between consecutive retry attempts.
  * **Purpose**: Defines how the ACS should handle communication failures with the VISA_DS service, including the maximum number of retry attempts and the wait time between retries.

### Micrometer Metrics Configuration for Monitoring

Micrometer is a powerful library for application monitoring and metrics collection. It provides a flexible way to export
metrics to various monitoring systems. To configure Micrometer metrics in your application, you can use the following
YAML configuration:

```yaml
metrics:
  export:
    graphite:
      enabled: true
```

The above configuration snippet allows you to export metrics to a Graphite monitoring system. Graphite is a popular
time-series database that can store and visualize metrics data.

To customize and export metrics to different monitoring systems or databases, you can refer to
the [Micrometer documentation](https://micrometer.io/docs) for more details on available options and configurations.

By using Micrometer, you can collect, analyze, and visualize application metrics effectively, helping you gain insights
into your application's performance and behavior. It provides a seamless integration with various monitoring and
alerting systems, making it a valuable tool for monitoring your application's health and performance.

Feel free to explore the Micrometer documentation for more advanced configurations and integration options with your
preferred monitoring system.

### HSM Configuration

The High-Security Module (HSM) configuration allows you to specify the type of HSM gateway to use and its related
parameters. Currently, two types of HSM gateways are supported: "LunaHSM" and "NoOpHSM."

#### Configuration Options

#### Example Usage

```yaml
hsm:
  enabled_gateway: "NoOpHSM"
  gateway:
    luna:
      ip: "127.0.0.1"
      port: 8080
      timeout: 1
```

* `enabled_gateway` (string)

  - Description: Specifies the type of HSM gateway to enable.
  - Possible Values:
      - "LunaHSM": Enable the Luna HSM gateway.
      - "NoOpHSM": Enable the NoOp HSM gateway (No operation, a mock HSM for testing).

* `gateway` (object)

  - Description: Additional configuration options specific to the enabled HSM gateway.

For "LunaHSM" enabled_gateway:

- Description: If you have chosen to enable the "LunaHSM" gateway, you can further configure the Luna HSM connection
  parameters.
  - Properties:
      - ip (string): The IP address of the Luna HSM.
      - port (integer): The port number to connect to the Luna HSM.
      - timeout (integer): The timeout (in seconds) for the connection to the Luna HSM.


In the above example, the "LunaHSM" gateway is enabled, and the Luna HSM is configured with the IP address "127.0.0.1,"
port "8080," and a connection timeout of "1" second.

If you want to use the "NoOpHSM" gateway, you do not need to provide any additional configuration, as it is a mock HSM
used for testing purposes.

Please make sure to choose the appropriate HSM gateway type and provide the relevant configuration parameters based on
your use case and requirements. If you have any further questions or need assistance, feel free to reach out to our
support team.

### Spring Configuration

#### Example Usage
```yaml
spring:
  datasource:
    url: jdbc:mysql://${ACS_MYSQL_HOST:localhost}:${ACS_MYSQL_PORT:3306}/${ACS_MYSQL_DATABASE:cas_db}
    username: ${ACS_MYSQL_USER:root}
    password: ${ACS_MYSQL_PASSWORD:password}
    driverClassName: com.mysql.cj.jdbc.Driver
```

* `datasource`:
  * **Description**: Configures the data source properties for connecting to a MySQL database.
  * **Properties**:
    * `url`: The JDBC URL for the MySQL database, allowing flexibility through environment variables.
    * `username`: The username for authenticating with the database.
    * `password`: The password for authenticating with the database.
    * `driverClassName`: The JDBC driver class for MySQL.
  * **Purpose**: Defines the connection details for accessing the MySQL database.

### Logging Configuration
#### Example Usage
```yaml
logging:
  level:
    org.hibernate.SQL: DEBUG
    org.springframework: INFO
    org.freedomfinancestack: INFO
    org.hibernate.type.descriptor.sql: TRACE
  pattern:
    level: "%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]"
```

* `level`:
  * **Description**: Configures logging levels for specific packages.
  * **Properties**:
    * `org.hibernate.SQL`: Sets the logging level for Hibernate SQL statements to DEBUG.
    * `org.springframework`: Sets the logging level for the Spring Framework to INFO.
    * `org.freedomfinancestack`: Sets the logging level for Freedom Finance Stack to INFO.
    * **Purpose**: Adjusts the logging levels for various packages, allowing control over the verbosity of logs.

* `pattern.level`:
  * **Description**: Configures the log pattern to include the application name, trace ID, and span ID.
  * **Purpose**: Enhances log readability by including relevant information like application name, trace ID, and span ID.

### Auth-Value Configuration
#### Example Usage
```yaml
auth-value:
  master-card-acs-key: ${MC_ACS_KEY:B039878C1F96D212F509B2DC4CC8CD1BB039878C1F96D212F509B2DC4CC8CD1B}
```
* `auth-value.master-card-acs-key`:
  * **Description**: Specifies the Mastercard ACS key used for authentication.
  * **Purpose**: Provides the authentication key required for Mastercard ACS.


### Task Configuration
#### Example Usage
```yaml
task:
  scheduler:
    corePoolSize: 5
    maxPoolSize: 10
    keepAliveTime: 60000 #mili
```
  * `task.scheduler`:
    * **Description**: Configures the scheduler settings for background tasks.
    * **Properties**:
      * `corePoolSize`: Sets the core pool size for the scheduler.
      * `maxPoolSize`: Sets the maximum pool size for the scheduler.
      * `keepAliveTime`: Sets the keep-alive time for idle threads.

### External-Libs Configuration
#### Example Usage
```yaml
external-libs:
  security:
    SecurityModuleAWS:
      enabled: false
    SecurityModuleAzure:
      enabled: false
  request-parsing:
    RequestParsingModuleAWS:
      enabled: false
```

* `external-libs`:
  * **Description**: Configures external libraries and modules.
  * **Properties**:
    * `security.SecurityModuleAWS.enabled`: Controls whether the AWS Security Module is enabled.
    * `security.SecurityModuleAzure.enabled`: Controls whether the Azure Security Module is enabled.
    * `request-parsing.RequestParsingModuleAWS.enabled`: Controls whether the AWS Request Parsing Module is enabled. 

### Institution-UI Configuration
#### Example Usage
```yaml
institution-ui:
  institution-url: https://ffs.acs.com/acs/resources/images/
  institution-css-url: ${INSTITUTION_CSS_URL:https://EMV3DS/challenge}
  html-page-timer: 180 #seconds
  medium-logo: "https://dummylink.com"
  high-logo: "https://dummylink.com"
  extra-high-logo: "https://dummylink.com"
  html-otp-template: "acsOtpNew"
  network-ui-config:
    VISA:
      medium-ps: "https://dummylink.com"
      high-ps: "https://dummylink.com"
      extra-high-ps: "https://dummylink.com"
    MASTERCARD:
      medium-ps: "https://dummylink.com"
      high-ps: "https://dummylink.com"
      extra-high-ps: "https://dummylink.com"
```
*  `institution-ui`:
*  **Description**: Configures settings related to the institution's user interface.
*  **Properties**:
  * `institution-url`: Specifies the URL for institution (Bank) images.
  * `institution-css-url`: Specifies the URL for institution (Bank) CSS.
  * `html-page-timer`: Sets the timer duration for HTML pages.
  * `medium-logo`, `high-logo`, `extra-high-logo`: URLs for different-sized institution (Bank) logos.
  * `html-otp-template`: Specifies the HTML template for OTP (One-Time Password) emails. 

### Encryption Configuration
#### Example Usage
```yaml
encryption:
  aes:
    password: ${ENCRYPTION_AES_PASSWORD:password}
    salt: ${ENCRYPTION_AES_SALT:salt}
```
*  `encryption`:
*  **Description**: Configures encryption settings using Advanced Encryption Standard (AES).
*  **Properties**: 
*  * `aes.password`: Specifies the password for AES encryption.
    * `aes.salt`: Specifies the salt for AES encryption.
* **Purpose**: Sets up parameters for AES encryption used within the application.
* > This is CRITICAL information, it must be kept secure according to PCI DSS standards and not expose here

Feel free to customize these configuration attributes according to your specific ACS deployment requirements. The
acs.yml file allows you to fine-tune the ACS behavior and settings based on your needs.

## Conclusion
Congratulations! You now know how to deploy the ACS server using various methods and how to provide the acs.yml
configuration file for each deployment option. Enjoy using the ACS server and feel free to contribute to the project on
GitHub.

