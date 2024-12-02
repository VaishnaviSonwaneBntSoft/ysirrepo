# Automated Test that Compares a Java Application Running with Different Garbage Collectors

This repository contains GitHub Actions workflows for testing a Java web service using G1GC and ZGC garbage collectors. The workflows automate the process of building the application, running performance tests using k6, and analyzing garbage collection logs.

---

## Purpose

The repository is designed to:
- Automate testing of web service using the garbage collectors (G1GC , ZGC).
- Analyze garbage collection logs using GCeasy API.
- Generate and publish results for review.

## Prerequisites

Before starting, ensure you have the following installed:

- **OpenJDK (Recommended version: OpenJDK 21 or later)**
- **Docker**
- **Gradle**
- **K6**
- **Git Installed Locally**
- **GitHub Account**

---

## Key Features
- **Garbage Collector Support:** Supports both G1GC and ZGC.
- **Performance Testing:** Uses k6 to run performance tests.
- **GC Analysis:** Uploads GC logs to GCEasy for detailed analysis.
- **Artifact Management:** Saves test results, GC logs, and analysis for further review.

## How to Use

### 1. **Build Process:**

   #### 1.1 Clone the Repository
       git clone https://github.com/yogeshwar-vhatkar-bnt/yogeshwar-trivy-demo-main
   
   #### 1.2 Directory Structure
      
      ├── tests                    # Directory containing k6 test scripts
      │   ├── greet-test.js         # k6 test script for greeting endpoint
      │   └── health-test.js        # k6 test script for health check endpoint
      ├── Dockerfile-g1gc           # Dockerfile for G1GC configuration
      ├── Dockerfile-zgc            # Dockerfile for ZGC configuration
      ├── build.gradle              # Gradle build file
      ├── settings.gradle           # Gradle settings file
      └── .github/workflows         # Directory containing GitHub Actions workflows
          ├── g1gc-test.yml         # GitHub Actions workflow for G1GC testing
          └── zgc-test.yml          # GitHub Actions workflow for ZGC testing

   #### 1.3 Build the Project Using Gradle
    ./gradlew clean build

### 2. **Configure Secrets:**

   Add the required secrets (`GHCR_PAT` and `GCEASY_API_KEY`) to the repository settings.

   #### 2.1 How to achieve this secrets 
   - `GHCR_PAT`: Personal Access Token for GitHub Container Registry. You can generate it from [GitHub's documentation on creating a Personal Access Token]                                      (https://docs.github.com/en/github/authenticating-to-github/creating-a-personal-access-token).
            
   - `GCEASY_API_KEY`: API key for accessing GCEasy. You can obtain the key by signing up on [GCEasy.io](https://gceasy.io) and requesting the API key.

### 3. **Run Workflow:**

   #### 3.1 The location of this can be found in
   - Please refer to section 1.2 Directory Structure in the README for the workflows.
               
   #### 3.2 What triggers the workflow       
   - Push to the ```main``` branch: Whenever code is pushed to the main branch, the workflow will automatically run.

   - Pull Request to the ```main``` branch: If a pull request is created or updated for the main branch, the workflow will also be triggered automatically.

### 4. **View Results:**

   #### 4.1 You can find it in the following location
   - Test results and GC analysis will be available as workflow artifacts in the following formats:
      - For **G1GC**, the results will be saved in a zip folder named `g1gc-results`.
      - For **ZGC**, the results will be saved in a zip folder named `gzgc-results`.
           
   - The artifacts will include:
     - **GC Logs** (e.g., `gzgc-gc.log`)
     - **GC Analysis Reports** (e.g., `gzgc-gc-analysis_report.txt`)
     - **Test Results** (e.g., `gzgc-test-results.json`)
     - **Performance Metrics**
           
   - These zip folders can be downloaded from the workflow run page.
         
## Workflow Overview

### Workflow for G1GC
- **Dockerfile:** `Dockerfile-g1gc`
- **Garbage Collector:** G1GC
- **Java Options:**
  - `-XX:+UseG1GC`: Enables the G1 Garbage Collector.
  - `-XX:MaxGCPauseMillis=200`: Sets the maximum pause time goal for G1GC to 200 milliseconds.
  - `-XX:G1HeapRegionSize=1m`: Configures the size of G1GC heap regions to 1 MB.
  - `-XX:+UseStringDeduplication`: Enables string deduplication in G1GC.
  - `-Xlog:gc*:file=gc.log:time,uptime,level,tags`: Logs GC events to `gc.log` with detailed timestamps, uptime, log level, and tags.
- **Here Dockerfile for g1gc**
     ```bash
      FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

      LABEL authors="yogeshwar.vhatkar"

      WORKDIR /app

      COPY build/libs/trivydemo-0.0.1-SNAPSHOT.jar trivy-demo-g1gc.jar

      EXPOSE 8080

      ENTRYPOINT ["java", "-Xms2G", "-Xmx2G", "-XX:MaxMetaspaceSize=1G", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-XX:G1HeapRegionSize=1m", "-XX:+UseStringDeduplication", "-               verbose:gc", "-Xlog:gc*:file=gc.log:time,uptime,level,tags", "-jar", "trivy-demo-g1gc.jar"]
     ```
   
### Workflow for ZGC
- **Dockerfile:** `Dockerfile-zgc`
- **Garbage Collector:** ZGC
- **Java Options:**
  - `-XX:+UseZGC`: Enables the Z Garbage Collector.
  - `-XX:+ZGenerational`: Enables the generational mode in ZGC.
  - `-XX:MaxGCPauseMillis=200`: Sets the maximum pause time goal for ZGC to 200 milliseconds.
  - `-XX:+UseStringDeduplication`: Enables string deduplication in ZGC.
  - `-Xlog:gc*:file=gc.log:time,uptime,level,tags`: Logs GC events to `gc.log` with detailed timestamps, uptime, log level, and tags.
- **Here Dockerfile for gzgc**
     ```bash
      FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

      LABEL authors="yogeshwar.vhatkar"

      WORKDIR /app

      COPY build/libs/trivydemo-0.0.1-SNAPSHOT.jar trivy-demo-gzgc.jar

      EXPOSE 8080

      ENTRYPOINT ["java", "-Xms2G", "-Xmx2G", "-XX:MaxMetaspaceSize=1G", "-XX:+UseZGC", "-XX:+ZGenerational", "-XX:MaxGCPauseMillis=200", "-XX:+UseStringDeduplication", "-verbose:gc",          "-Xlog:gc*:file=gc.log:time,uptime,level,tags", "-jar", "trivy-demo-gzgc.jar"]
     ```

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

11. **Analyze GC logs:** The garbage collection logs are sent to GCEasy for analysis. GCEasy returns a JSON file containing detailed performance metrics. From this JSON file, we extract the following key factors:

| **Factor**              | **Description**                                                     | **Desired Value**                                             |
|-------------------------|---------------------------------------------------------------------|---------------------------------------------------------------|
| **Average Pause Time**   | The average time spent on GC pauses.                                | Less than 100ms                                                |
| **Max Pause Time**       | The longest GC pause.                                              | Less than 200ms, ideally under 1 second                        |
| **Throughput Percentage**| The percentage of time spent performing application work vs. GC.   | Greater than 90%                                               |
| **Minor GC Count**       | The number of minor GC events.                                      | As few as possible                                             |
| **Full GC Count**        | The number of full GC events.                                       | Keep it to a minimum, ideally zero or very few                |
| **Average Allocation Rate** | The rate at which memory is allocated.                             | Should be manageable without causing excessive GC overhead     |

   - These metrics are crucial for understanding the performance of the garbage collector.
     
12. **Upload artifacts:** Saves test results, GC logs, and analysis reports as workflow artifacts.
    
13. **Stop and clean up containers:** Stops the running container and removes unused Docker resources.


## Troubleshooting
- **Authentication Errors:** Ensure `GHCR_PAT` and `GCEASY_API_KEY` are correctly configured in repository secrets.
- **Build Failures:** Verify the Gradle build configuration and dependencies.
- **Container Issues:** Check the Dockerfiles and ensure the application runs correctly within the container.
- **k6 Test Failures:** Review the test scripts located in the `tests` directory.

