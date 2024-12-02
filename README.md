# 50 - Test Web Service G1 GC

This repository contains a GitHub Actions workflow for testing a Java web service using G1GC and ZGC garbage collectors. The workflow automates the process of building the application, running performance tests using k6, and analyzing garbage collection logs.

## Workflow Overview
The workflow is triggered on `push` and `pull_request` events. It performs the following steps:

1. **Checkout the code:** Retrieves the repository content.
2. **Set up JDK:** Configures Java Development Kit (JDK 21) for building and running the application.
3. **Gather machine information:** Captures CPU and memory details for analysis.
4. **Set up Gradle:** Configures Gradle for building the Java application.
5. **Build the application:** Compiles the code using the Gradle wrapper.
6. **Log in to GitHub Container Registry:** Authenticates with GitHub's container registry.
7. **Build and run the container:** Builds Docker images using `Dockerfile-g1gc` and `Dockerfile-zgc` and runs them.
8. **Setup k6:** Installs the k6 load testing tool.
9. **Run k6 tests:** Executes API tests using k6 and generates test results.
10. **Extract GC logs:** Copies garbage collection logs (`gc.log`) from the containers.
11. **Analyze GC logs:** Sends the logs to [GCEasy](https://gceasy.io/) for analysis and extracts performance metrics.
12. **Upload artifacts:** Saves test results and analysis reports as workflow artifacts.
13. **Stop and clean up containers:** Stops the running containers and removes unused Docker resources.

## Key Features
- **Garbage Collector Support:** Supports both G1GC and ZGC configurations.
- **Performance Testing:** Uses k6 to run performance tests.
- **GC Analysis:** Uploads GC logs to GCEasy for detailed analysis.
- **Artifact Management:** Saves test results and analysis for further review.

## Required Secrets
- `GHCR_PAT`: Personal Access Token for GitHub Container Registry.
- `GCEASY_API_KEY`: API key for accessing GCEasy.

## Directory Structure
```
.
├── tests                # Directory containing k6 test scripts
├── Dockerfile-g1gc      # Dockerfile for G1GC configuration
├── Dockerfile-zgc       # Dockerfile for ZGC configuration
├── build.gradle         # Gradle build file
├── settings.gradle      # Gradle settings file
└── .github/workflows    # Directory containing GitHub Actions workflows
```

## Dockerfile Details

### Dockerfile-g1gc
```dockerfile
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

LABEL authors="yogeshwar.vhatkar"

WORKDIR /app

COPY build/libs/trivydemo-0.0.1-SNAPSHOT.jar trivy-demo-g1gc.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xms2G", "-Xmx2G", "-XX:MaxMetaspaceSize=1G", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-XX:G1HeapRegionSize=1m", "-XX:+UseStringDeduplication", "-verbose:gc", "-Xlog:gc*:file=gc.log:time,uptime,level,tags", "-jar", "trivy-demo-g1gc.jar"]
```

### Dockerfile-zgc
```dockerfile
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

LABEL authors="yogeshwar.vhatkar"

WORKDIR /app

COPY build/libs/trivydemo-0.0.1-SNAPSHOT.jar trivy-demo-gzgc.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xms2G", "-Xmx2G", "-XX:MaxMetaspaceSize=1G", "-XX:+UseZGC", "-XX:+ZGenerational", "-XX:MaxGCPauseMillis=200", "-XX:+UseStringDeduplication", "-verbose:gc", "-Xlog:gc*:file=gc.log:time,uptime,level,tags", "-jar", "trivy-demo-gzgc.jar"]
```

## How to Use

1. **Clone the Repository:**
   ```bash
   git clone <repository-url>
   cd <repository-name>
   ```

2. **Configure Secrets:**
   Add the required secrets (`GHCR_PAT` and `GCEASY_API_KEY`) to the repository settings.

3. **Run Workflow:**
   Push changes to the repository or create a pull request to trigger the workflow.

4. **View Results:**
   - Test results and GC analysis will be available as workflow artifacts.
   - GC logs and performance metrics are saved in GCEasy.

## Workflow File
The complete workflow file is located at `.github/workflows/test-web-service.yml`. The file is designed for reusability and can be modified to suit additional requirements.

## Troubleshooting
- **Authentication Errors:** Ensure `GHCR_PAT` and `GCEASY_API_KEY` are correctly configured in repository secrets.
- **Build Failures:** Verify the Gradle build configuration and dependencies.
- **Container Issues:** Check the Dockerfiles and ensure the application runs correctly within the containers.
- **k6 Test Failures:** Review the test scripts located in the `tests` directory.

## License
This project is licensed under the [MIT License](LICENSE).

---
For questions or support, please contact the repository maintainer.
