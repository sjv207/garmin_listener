# Garmin Listener Application

This is a Spring Boot application that listens for HTTP GET requests on the `/listen` endpoint.

## Architecture

- `GarminListenerApplication.java`: The main Spring Boot application class.
- `ListenController.java`: A REST controller that handles requests to the `/listen` endpoint. It accepts any query parameters and logs them to the console.
- `pom.xml`: The Maven project configuration. It includes the `spring-boot-starter-web` dependency.
- `Dockerfile`: A Dockerfile to build a Docker image for the application.

## Development

### Building the application

To build the application, run the following command:

```bash
mvn clean install
```

### Running the application

To run the application, use the following command:

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8085`.

### Running in Docker

To build the Docker image, run:

```bash
docker build -t garmin-listener .
```

To run the Docker container, run:
```bash
docker run -p 8085:8085 garmin-listener
```

### Testing the endpoint

You can test the endpoint by sending a GET request to `http://localhost:8080/listen` with some query parameters. For example:

```bash
curl "http://localhost:8085/listen?param1=value1&param2=value2"
```
