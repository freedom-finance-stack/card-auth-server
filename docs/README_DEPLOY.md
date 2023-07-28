# Card Auth Server (ACS)

The Card Auth Server (ACS) is a powerful and flexible server that provides authentication services for card transactions. This README provides instructions on how to deploy the ACS server using various methods and how to provide the `acs.yml` configuration file for each deployment option.

## Deployment Options

The ACS server can be deployed using the following methods:

1. Running the JAR Command
2. Docker Run
3. Docker Compose
4. Kubernetes
5. Using Built-In Automated Shell Script

For each deploym    ent option, you can provide the `acs.yml` configuration file to customize the server's behavior and settings.

### 1. Running the JAR Command

To run the ACS server using the JAR command, use the following command:

```bash
java -jar card-auth-server-acs.jar --spring.config.additional-location=/path/to/acs.yml
```

Replace /path/to/acs.yml with the path to your custom acs.yml configuration file. This command will start the ACS server and load the configuration from the provided acs.yml file.

### 2. Docker Run
To deploy the ACS server using Docker, you can copy the acs.yml configuration file to a specific path inside the container using the -v (volume) option. Here's an example command:

```bash
docker run -v /path/to/acs.yml:/path/inside/container/acs.yml your-image-name
```

Replace /path/to/acs.yml with the path to your custom acs.yml configuration file on the host machine and your-image-name with the name of your ACS Docker image.

### 3. Docker Compose

To use Docker Compose for deploying the ACS server, you can set environment variables in the docker-compose-dev.yaml file. Docker Compose dev is located [here](https://github.com/razorpay/card-auth-server/blob/master/scripts/deployment/dockerconf/card-auth-server-acs/docker-compose-dev.yaml).

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
              mountPath: /path/inside/container/acs.yml
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
Note: use absolute path for config file 

### ACS Configuration (acs.yml) Explained
Below is the explanation of the attributes present in the acs.yml configuration file:
```yaml
acs:
  referenceNumber: 99995678901234567890

```

```yaml
metrics:
  export:
    graphite:
      enabled: true

```

metrics: This section contains the configuration related to metrics.
export: This subsection specifies the metrics export settings.
graphite: This subsection specifies the Graphite metrics exporter settings.
enabled: This attribute indicates whether Graphite metrics exporting is enabled (true) or not (false).
```yaml
hsm:
  enabled_gateway: "NoOpHSM"
  gateway:
    luna:
      ip: "127.0.0.1"
      port: 8080
      timeout: 1

```

hsm: This section contains the configuration related to the Hardware Security Module (HSM).
enabled_gateway: This attribute specifies the HSM gateway to be used. In this example, it is set to "NoOpHSM", indicating the use of the NoOp (No Operation) HSM, which means no real HSM is configured.
gateway: This subsection contains the specific HSM gateway settings.
luna: This subsection specifies the configuration for the Luna HSM gateway.
ip: This attribute defines the IP address of the Luna HSM gateway. In this example, it is set to "127.0.0.1", which is a loopback address indicating that the HSM gateway is running on the same machine.
port: This attribute defines the port number on which the Luna HSM gateway is listening. In this example, it is set to 8080.
timeout: This attribute specifies the timeout duration (in seconds) for HSM operations. In this example, it is set to 1 second.
Feel free to customize these configuration attributes according to your specific ACS deployment requirements. The acs.yml file allows you to fine-tune the ACS behavior and settings based on your needs.

### Conclusion
Congratulations! You now know how to deploy the ACS server using various methods and how to provide the acs.yml configuration file for each deployment option. Enjoy using the ACS server and feel free to contribute to the project on GitHub.

