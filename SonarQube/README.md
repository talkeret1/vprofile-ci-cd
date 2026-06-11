# SonarQube Code Quality

## 📌 Overview

SonarQube is used for static code analysis in the CI/CD pipeline.

---

## 🚀 Access

http://localhost:9000


Default login:
- Username: `admin`
- Password: `admin`

---

## 📦 Project Setup

1. Create project manually:
   - Name: `vprofile`

2. Add webhook:

   - http://host.docker.internal:8080/sonarqube-webhook/


---

## 🔐 Jenkins Integration

### Add Sonar Token in Jenkins
- Manage Jenkins → Credentials
- Kind: Secret Text
- ID: `sonarqube-token`

---

### Configure SonarQube Server in Jenkins
- Name: `sonarqube`
- URL: `http://host.docker.internal:9000`
- Token: `sonarqube-token`

---

## 🧪 Scanner Configuration

Tool name in Jenkins:

SonarScanner

---

## 📸 Screenshots

- `screenshots/sonarqube-dashboard.png`
- `screenshots/sonarqube-project.png`
- `screenshots/sonarqube-quality-gate.png`

---

## 🔁 Pipeline Flow

- Jenkins triggers analysis
- SonarQube runs scan
- Quality gate validates results
- Jenkins continues or fails pipeline
