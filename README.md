# VProfile - CI/CD DevOps Project (Junior Portfolio)

<br>

## 📌 Overview

<br>

This project is a full DevOps CI/CD pipeline for deploying a Java Spring-based application into AWS ECS using Docker, Terraform, Jenkins, and SonarQube.

The application itself is not a production-ready system, but a **DevOps training project** originally taken from Udemy and based on the work of **Imran Teli**:
https://github.com/hkhcoder/vprofile-project

The goal of this project is to validate integration between multiple services:
- MySQL (RDS)
- Memcached
- RabbitMQ
- OpenSearch
- Jenkins CI/CD pipeline
- SonarQube code analysis

<br>
<br>
<br>

---

## 🧠 Application Purpose

<br>

The application is used to verify that infrastructure components are working correctly:

### 1. *Database Validation:*
**Login page:**
- Username: `admin_vp`
- Password: `admin_vp`

If login succeeds → MySQL connection is working.

📸 Screenshot: `screenshots/login-success.png`

<br><br>

### 2. *Memcached Validation:*
**After login:**
- Click **All Users** button:
- Open a user profile

You will see:
- First load:  
  `[Data is From DB and Data Inserted In Cache !!]`
- Second load:  
  `[Data is From Cache]`

This confirms Memcached caching works.

📸 Screenshot: `screenshots/memcached-demo.png`

<br><br>

### 3. *RabbitMQ Validation:*
Click **RabbitMQ** button:

Expected output:

`RabbitMQ Initiated`  
`Generated 7 Connections`  
`2 Channels, 3 Exchanges, and 6 Queues`  

📸 Screenshot: `screenshots/rabbitmq.png`

<br><br>

### 4. *Elasticsearch / OpenSearch Validation:*
Click **Index Users** button:

Expected result:

`SUCCESS into OpenSearch`  
`Environment: AWS Production`  
`Search Engine: OpenSearch` 


📸 Screenshot: `screenshots/opensearch.png`

<br>
<br>
<br>

---

## 🏗️ Architecture

<br>

- Docker Compose (local development)
- Jenkins CI/CD pipeline
- AWS ECS Fargate deployment
- Terraform infrastructure provisioning

<br>
<br>
<br>

---

## 🚀 Deployment Options

<br>

### Option 1: Local Docker Compose

Run full stack locally:

```bash
docker-compose up -d
```

Includes:

- App
- MySQL
- Memcached
- RabbitMQ
- Nginx / Web layer

<br>

### Option 2: Jenkins CI/CD → AWS ECS

Pipeline:

- Build WAR with Maven
- Run Checkstyle
- SonarQube analysis
- Build Docker image
- Push to ECR
- Deploy to ECS

📸 Screenshot: screenshots/jenkins-pipeline.png

<br>

### Option 3: Infrastructure with Terraform

Terraform provisions:

- VPC
- ECS Cluster (Fargate)
- RDS MySQL
- ElastiCache (Memcached)
- RabbitMQ (Amazon MQ)
- OpenSearch
- Security Groups & IAM roles

Run:

```bash
cd terraform
terraform init
terraform apply
```

📸 Screenshot: screenshots/terraform-apply.png

<br>
<br>
<br>

---

## 📦 Technologies Used

<br>

- Java 17 + Spring
- Maven
- Docker / Docker Compose
- AWS (ECS, ECR, RDS, MQ, OpenSearch)
- Terraform
- Jenkins
- SonarQube
- Trivy

<br>

📌 **Notes**

This project is intended for DevOps learning and portfolio demonstration only.

<br>
<br>
<br>

---

## 🙏 Credits

<br>

Original project:
https://github.com/hkhcoder/vprofile-project

Author: Imran Teli