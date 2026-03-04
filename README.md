# Web Framework for REST Services and Static File Management

A lightweight Java-based web framework that enables developers to create REST services using lambda expressions and serve static files. This framework provides a simple and intuitive API inspired by modern web frameworks, allowing rapid development of web applications with minimal boilerplate code.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

You need to have the following software installed on your system:

* **Java JDK 17** or higher
* **Maven 3.6+** - Dependency management and build tool
* **Git** - Version control system

To verify your installations, run:

```bash
java -version
mvn -version
git --version
```

### Installing

Follow these steps to get a development environment running:

**1. Clone the repository**

```bash
git clone https://github.com/Rogerrdz/Web-Framework-Development-for-REST-Services-and-Static-File-Management.git

cd Web-Framework-Development-for-REST-Services-and-Static-File-Management
```

**2. Compile the project**

```bash
mvn clean compile
```

**3. Run the application**

```bash
mvn exec:java
```

**4. Verify the server is running**

You should see the following output:
```
Listo para recibir ...
```

The server is now listening on `http://localhost:8080`

**5. Test the application**

Open your browser and navigate to:
```
http://localhost:8080/index.html
```

You should see the welcome page with links to all available REST services.

## Architecture

### Core Components

The framework consists of the following key components:

* **WebFramework.java** - Core routing engine that maps URLs to lambda functions
* **HttpServer.java** - HTTP server implementation that handles incoming requests
* **HttpRequest.java** - Request object that provides access to query parameters
* **HttpResponse.java** - Response object for sending data back to clients
* **WebMethod.java** - Functional interface for defining REST service handlers

### How It Works

1. **Route Registration**: Routes are registered using the `get()` method with a path and lambda expression
2. **Request Processing**: Incoming HTTP requests are parsed and matched against registered routes
3. **Lambda Execution**: Matching routes execute their associated lambda functions
4. **Response Generation**: Results are wrapped in HTTP responses and sent to clients
5. **Static File Serving**: Non-matched routes check for static files in the configured directory

### Example Usage

Here's how to create a simple web application:

```java
import static WebFramework.WebFramework.get;

public class App {
    public static void main(String[] args) throws IOException, URISyntaxException {
        // Configure static files location
        HttpServer.staticfiles("webroot");
        
        // Define REST endpoints
        get("/pi", (req, res) -> String.valueOf(Math.PI));
        get("/euler", (req, res) -> String.valueOf(Math.E));
        get("/hello", (req, res) -> "Hello " + req.getValues("name"));
        
        // Start the server
        HttpServer.main(args);
    }
}
```

## Running the tests

To run the automated tests for this system:

```bash
mvn test
```

### Break down into end to end tests

The test suite includes unit tests for core functionality:

**Testing Route Registration**
```java
@Test
public void testGetRouteRegistration() {
    WebFramework.get("/test", (req, res) -> "test response");
    assertNotNull(WebFramework.getRoute("/test"));
}
```

**Testing Query Parameter Extraction**
```java
@Test
public void testQueryParameterExtraction() {
    Map<String, String> params = new HashMap<>();
    params.put("name", "Pedro");
    HttpRequest request = new HttpRequest(params);
    assertEquals("Pedro", request.getValues("name"));
}
```
## API Reference

### Core Methods

**Registering Routes**
```java
get(String path, WebMethod handler)
```
Registers a GET endpoint with a lambda handler.

**Configuring Static Files**
```java
staticfiles(String directory)
```
Sets the directory for serving static files.

**Accessing Query Parameters**
```java
req.getValues(String paramName)
```
Retrieves query parameter values from the request.

## Available Endpoints

Once the server is running, the following endpoints are available:

| Endpoint | Description | Example |
|----------|-------------|---------|
| `/pi` | Returns the value of PI | `http://localhost:8080/pi` |
| `/euler` | Returns the value of Euler's number | `http://localhost:8080/euler` |
| `/App/pi` | Returns PI with label | `http://localhost:8080/App/pi` |
| `/App/helloWorld` | Returns "Hello World" | `http://localhost:8080/App/helloWorld` |
| `/App/frommethod` | Returns Euler from method | `http://localhost:8080/App/frommethod` |
| `/App/hello` | Greets user by name | `http://localhost:8080/App/hello?name=Pedro` |
| `/index.html` | Serves static HTML page | `http://localhost:8080/index.html` |

## Deployment

To deploy this application on a live system:

**1. Package the application**
```bash
mvn clean package
```

**2. Configure the server port** (optional)

Edit `HttpServer.java` line 26 to change the port:
```java
serverSocket = new ServerSocket(8080); // Change to desired port
```

**3. Create a JAR with dependencies**

Add to your `pom.xml`:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <configuration>
        <archive>
            <manifest>
                <mainClass>WebFramework.App</mainClass>
            </manifest>
        </archive>
        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
    </configuration>
</plugin>
```

**4. Run the packaged application**
```bash
java -jar target/Web-Framework-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management and Build Tool
* [Java 17](https://www.oracle.com/java/) - Programming Language
* [JUnit 5](https://junit.org/junit5/) - Testing Framework

## Project Structure

```
Web-Framework/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── WebFramework/
│   │   │       ├── App.java
│   │   │       ├── HttpServer.java
│   │   │       ├── HttpRequest.java
│   │   │       ├── HttpResponse.java
│   │   │       ├── WebFramework.java
│   │   │       ├── WebMethod.java
│   │   │       └── examples/
│   │   │           └── MathServices.java
│   │   └── resources/
│   │       └── WebRoot/
│   └── test/
│       └── java/
│           └── WebFramework/
│               └── AppTest.java
├── webroot/
│   └── index.html
├── pom.xml
└── README.md
```

## Authors

* **Roger Rodriguez** - *Initial work and framework development*
* **Github - User** - Rogerrdz

## Acknowledgments

* Inspired by modern web frameworks like Express.js and Spark Java
* Built as part of the AREP (Arquitecturas Empresariales) course
* Special thanks to the Java community for excellent documentation
* Lambda expressions make this framework possible and elegant

## Troubleshooting

**Port already in use**
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (Windows)
taskkill /F /PID <process_id>
```

**Maven build fails**
```bash
# Clean and rebuild
mvn clean install -U
```

**Static files not found**
- Ensure files are in the `webroot/` directory
- Run `mvn compile` to copy resources to `target/classes/`