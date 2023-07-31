# Card Auth Server (ACS)

The Card Auth Server (ACS) is a powerful and flexible server that provides authentication services for card transactions. This README provides instructions on how to deploy the ACS server using various methods and how to provide the `acs.yml` configuration file for each deployment option.

## Deployment Options

The ACS server can be deployed using the following methods:

1. Running the JAR Command
2. Docker Run
3. Docker Compose
4. Kubernetes
5. Using Built-In Automated Shell Script

For each deploym    ent option, you can provide the `acs.yml` configuration file to customize the server's behavior and settings. find out more acs.yml [here](#acs-configuration-acsyml-explained)

### 1. Running the JAR Command

To run the ACS server using the JAR command, use the following command:

```bash
java -jar card-auth-server-acs.jar --spring.config.additional-location=/path/to/acs.yml
```

Replace /path/to/acs.yml with the path to your custom acs.yml configuration file. This command will start the ACS server and load the configuration from the provided acs.yml file.

### 2. Docker Run
To deploy the ACS server using Docker, you can copy the acs.yml configuration file to a specific path inside the container using the -v (volume) option. Here's an example command:

```bash
docker run -v /path/to/acs.yml:/opt/card-auth-server/cas-acs/bin/config/acs.yml card-auth-server-acs:latest
```

Replace /path/to/acs.yml with the path to your custom acs.yml configuration file on the host machine and card-auth-server-acs:latest with the name of your ACS Docker image.

### 3. Docker Compose

To use Docker Compose for deploying the ACS server, you can set environment variables in the docker-compose-dev.yaml file. Docker Compose dev is located [here](https://github.com/razorpay/card-auth-server/blob/master/scripts/deployment/dockerconf/card-auth-server-acs/docker-compose-dev.yaml).
make sure to run following command from root directory of project. 

~Here's an example:
```bash
 EXTERNAL_YAML_PATH=/path/to/acs.yml docker-compose -f ./scripts/deployment/dockerconf/card-auth-server-acs/docker-compose-dev.yaml up -d
 ```

### 4. Kubernetes

For Kubernetes deployment, you can create a ConfigMap to store the acs.yml configuration file and mount it to a specific path in the ACS container. Here's an example of how to create the ConfigMap:
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

if you want to use external yaml file, add EXTERNAL_YAML_PATH=/config/external/sample-acs.yml inside scripts/deployment/dev/cas-acs-dev-deployment.sh
Note: use absolute path for config file and make sure to run script from root directory of project.

## ACS Configuration (acs.yml) Explained
Below is the explanation of the attributes present in the acs.yml configuration file:

Sample acs.yml is located [here](https://github.com/freedom-finance-stack/card-auth-server/blob/master/config/external/sample-acs.yml).

### ACS (Card Auth Server) configuration.
The ACS (Access Control Server) configuration allows you to customize various settings for the Access Control Server. Below are the available configuration options:

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

  
### Micrometer Metrics Configuration for Monitoring

Micrometer is a powerful library for application monitoring and metrics collection. It provides a flexible way to export metrics to various monitoring systems. To configure Micrometer metrics in your application, you can use the following YAML configuration:

```yaml
metrics:
  export:
    graphite:
      enabled: true

```

The above configuration snippet allows you to export metrics to a Graphite monitoring system. Graphite is a popular time-series database that can store and visualize metrics data.

To customize and export metrics to different monitoring systems or databases, you can refer to the [Micrometer documentation](https://micrometer.io/docs) for more details on available options and configurations.

By using Micrometer, you can collect, analyze, and visualize application metrics effectively, helping you gain insights into your application's performance and behavior. It provides a seamless integration with various monitoring and alerting systems, making it a valuable tool for monitoring your application's health and performance.

Feel free to explore the Micrometer documentation for more advanced configurations and integration options with your preferred monitoring system.


### HSM Configuration

The High-Security Module (HSM) configuration allows you to specify the type of HSM gateway to use and its related parameters. Currently, two types of HSM gateways are supported: "LunaHSM" and "NoOpHSM."

#### Configuration Options

`enabled_gateway` (string)
- Description: Specifies the type of HSM gateway to enable.
- Possible Values:
  - "LunaHSM": Enable the Luna HSM gateway.
  - "NoOpHSM": Enable the NoOp HSM gateway (No operation, a mock HSM for testing).

`gateway` (object)
- Description: Additional configuration options specific to the enabled HSM gateway.


  For "LunaHSM" enabled_gateway:
- Description: If you have chosen to enable the "LunaHSM" gateway, you can further configure the Luna HSM connection parameters.
- Properties:
  - ip (string): The IP address of the Luna HSM.
  - port (integer): The port number to connect to the Luna HSM.
  - timeout (integer): The timeout (in seconds) for the connection to the Luna HSM.

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
In the above example, the "LunaHSM" gateway is enabled, and the Luna HSM is configured with the IP address "127.0.0.1," port "8080," and a connection timeout of "1" second.

If you want to use the "NoOpHSM" gateway, you do not need to provide any additional configuration, as it is a mock HSM used for testing purposes.

Please make sure to choose the appropriate HSM gateway type and provide the relevant configuration parameters based on your use case and requirements. If you have any further questions or need assistance, feel free to reach out to our support team.


Feel free to customize these configuration attributes according to your specific ACS deployment requirements. The acs.yml file allows you to fine-tune the ACS behavior and settings based on your needs.

## Conclusion
Congratulations! You now know how to deploy the ACS server using various methods and how to provide the acs.yml configuration file for each deployment option. Enjoy using the ACS server and feel free to contribute to the project on GitHub.

