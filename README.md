# Automated Test that Compares a Java Application Running with Different Garbage Collectors

This repository contains GitHub Actions workflows for testing a Java web service using G1GC and ZGC garbage collectors. The workflows automate the process of building the application, running performance tests using k6, and analyzing garbage collection logs.

## Workflow Overview

### Workflow for G1GC
- **Dockerfile:** `Dockerfile-g1gc`
- **Garbage Collector:** G1GC
- **Java Options:**
  - `-XX:+UseG1GC`
  - `-XX:MaxGCPauseMillis=200`
  - `-XX:G1HeapRegionSize=1m`
  - `-XX:+UseStringDeduplication`
  - `-Xlog:gc*:file=gc.log:time,uptime,level,tags`

### Workflow for ZGC
- **Dockerfile:** `Dockerfile-zgc`
- **Garbage Collector:** ZGC
- **Java Options:**
  - `-XX:+UseZGC`
  - `-XX:+ZGenerational`
  - `-XX:MaxGCPauseMillis=200`
  - `-XX:+UseStringDeduplication`
  - `-Xlog:gc*:file=gc.log:time,uptime,level,tags`

## Steps in Both Workflows
The workflows are triggered on `push` and `pull_request` events. They perform the following steps:

1. **Checkout the code:** Retrieves the repository content.
2. **Set up JDK:** Configures Java Development Kit (JDK 21) for building and running the application.
3. **Gather machine information:** Captures CPU and memory details for analysis.
4. **Set up Gradle:** Configures Gradle for building the Java application.
5. **Build the application:** Compiles the code using the Gradle wrapper.
6. **Log in to GitHub Container Registry:** Authenticates with GitHub's container registry.
7. **Build and run the container:** Builds a Docker image and runs it using the respective Dockerfile (`Dockerfile-g1gc` or `Dockerfile-zgc`).
8. **Setup k6:** Installs the k6 load testing tool.
9. **Run k6 tests:** Executes API tests using k6 and generates test results.
10. **Extract GC logs:** Copies garbage collection logs from the container.
11. **Analyze GC logs:** Sends the logs to [GCEasy](https://gceasy.io/) for analysis and extracts performance metrics.
12. **Upload artifacts:** Saves test results and analysis reports as workflow artifacts.
13. **Stop and clean up containers:** Stops the running container and removes unused Docker resources.

## Key Features
- **Garbage Collector Support:** Supports both G1GC and ZGC.
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

## Troubleshooting
- **Authentication Errors:** Ensure `GHCR_PAT` and `GCEASY_API_KEY` are correctly configured in repository secrets.
- **Build Failures:** Verify the Gradle build configuration and dependencies.
- **Container Issues:** Check the Dockerfiles and ensure the application runs correctly within the container.
- **k6 Test Failures:** Review the test scripts located in the `tests` directory.

## License
This project is licensed under the [MIT License](LICENSE).

---
For questions or support, please contact the repository maintainer.
