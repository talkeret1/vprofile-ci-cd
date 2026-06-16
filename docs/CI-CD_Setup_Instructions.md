# CI/CD Setup Instructions

This document describes the minimum configuration required before running the Jenkins CI/CD pipeline.

---

## Step 1: Launch Jenkins

```bash
cd Jenkins
docker-compose up -d
```

Wait until Jenkins becomes available:

```text
http://localhost:8080
```

Complete the Jenkins configuration described in:

```text
Jenkins/README.md
```

---

## Step 2: Launch SonarQube

```bash
cd SonarQube
docker-compose up -d
```

Wait until SonarQube becomes available:

```text
http://localhost:9000
```

Complete the SonarQube configuration described in:

```text
SonarQube/README.md
```

---

## Step 3: Create AWS Resources

### Create ECR Repository

Repository name:

```text
vprofile/app
```

### Create IAM User

Attach the following policies:

* AmazonEC2ContainerRegistryFullAccess
* AmazonECS_FullAccess

Generate:

* Access Key ID
* Secret Access Key

These credentials will be added to Jenkins later.

---

## Step 4: Configure Jenkins AWS Credentials

Open:

```text
Manage Jenkins
→ Credentials
→ System
→ Global credentials
```

Add:

```text
Kind: AWS Credentials
ID: aws-credentials
```

Use the Access Key and Secret Key created in AWS.

---

## Step 5: Provision Infrastructure with Terraform

Terraform provisions:

* VPC
* Public and Private Subnets
* Application Load Balancer
* ECS Cluster (Fargate)
* RDS MySQL
* ElastiCache (Memcached)
* Amazon MQ (RabbitMQ)
* OpenSearch
* Security Groups
* IAM Roles
* Bastion Host

Run:

```bash
cd terraform

terraform init
terraform apply
```

Detailed Terraform instructions are available in:

```text
terraform/README.md
```

---

## Step 6: Update Jenkinsfile

After Terraform finishes, collect the following outputs:

* private_subnets
* ecs_security_group

Update the Jenkinsfile:

```groovy
PRIVATE_SUBNETS = "subnet-xxxxxxxx,subnet-yyyyyyyy"
ECS_SECURITY_GROUP = "sg-xxxxxxxx"
```

These values are required by the ECS deployment stage.

---

## Step 7: Run the Pipeline

Create a Pipeline job in Jenkins and point it to:

```text
https://github.com/<your-repository>
```

Run the pipeline.

Successful execution will:

1. Build the application.
2. Run Checkstyle.
3. Run SonarQube analysis.
4. Verify the Quality Gate.
5. Build the Docker image.
6. Run Trivy security scan.
7. Push the image to ECR.
8. Deploy the application to ECS Fargate.

---

## Additional Documentation

* Jenkins Setup → Jenkins/README.md
* SonarQube Setup → SonarQube/README.md
* Terraform Infrastructure → terraform/README.md
