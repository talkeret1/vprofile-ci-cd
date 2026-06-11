# Jenkins CI/CD Pipeline

## 📌 Overview

This Jenkins pipeline builds, tests, scans, containerizes, and deploys the VProfile application to AWS ECS.

---

## ⚙️ Pipeline Stages

### 1. Clone Repository
Pulls source code from GitHub.

---

### 2. Code Quality
Runs Checkstyle validation:

```bash
mvn validate checkstyle:checkstyle
```

### 3. Build Application

Compiles and packages WAR file:

```bash
mvn clean verify
```

4. SonarQube Analysis

Performs static code analysis.

📌 Requires:

SonarScanner tool in Jenkins
SonarQube server integration
5. Quality Gate

Pipeline stops if quality gate fails.

6. Docker Build

Builds Docker image:

```bash
docker build -t vprofile/app .
```

7. Trivy Scan

Security scan for vulnerabilities.

8. Push to AWS ECR

Authenticates and pushes image to ECR.

9. Deploy to AWS ECS

Updates ECS service:

First deploy → sets desired count = 1
Later deploys → force new deployment


🔐 Jenkins Credentials Required

AWS Credentials
ID: aws-credentials

Permissions:

ECR full access
ECS full access


SonarQube Token
ID: sonarqube-token


⚙️ Jenkins Tools
Maven 3.9.15
SonarScanner

📸 Screenshots
- screenshots/jenkins-dashboard.png
- screenshots/jenkins-pipeline.png


🚀 How to Run
1. Start Jenkins container
2. Install plugins from plugins.txt
3. Configure credentials
4. Run pipeline job