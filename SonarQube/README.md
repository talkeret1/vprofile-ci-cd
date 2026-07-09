# SonarQube Code Quality

<br>

## 📌 Overview

<br>

SonarQube is used as the static code analysis engine within the CI/CD pipeline to detect code quality issues, security vulnerabilities, and maintainability risks before deployment.

Analysis is executed automatically as part of the Jenkins pipeline.

<br>

## 🧭 CI/CD Analysis Flow

Source Code → Jenkins Pipeline → Sonar Scanner → SonarQube → Quality Gate → Pipeline Decision

<br>

## 🔍 Analysis Process

Jenkins pipeline executes SonarQube analysis as a dedicated CI/CD stage:

1. **Sonar Scanner Execution**: Jenkins triggers the Sonar Scanner to analyze the application source code and generate analysis data.
2. **Analysis Report Generation**: The scanner produces a detailed report including:
   - Code quality metrics
   - Security vulnerability findings
   - Code smell detection
   - Test coverage evaluation
3. **Report Processing**: The analysis report is sent to SonarQube for evaluation.
4. **Quality Gate Check**: SonarQube applies predefined rules to determine pipeline success or failure.

<br>

## Quality Gate

<br>

The Quality Gate enforces predefined quality thresholds to ensure production readiness.

The pipeline will fail if any of the following conditions are not met:

- Presence of blocker or critical bugs
- Security vulnerabilities above allowed threshold
- Excessive code smells affecting maintainability
- Code coverage below the defined minimum standard

<br>

The Quality Gate acts as a final validation step before deployment to AWS infrastructure.

<br>

---

⬅️ [Back to README](../README.md)