# Setup Instructions

<br>

To Run This Project You need to Setup Jenkins and SonarQube Docker Containers and Configure AWS Environment, Follow the Steps Below:

<br>

## Step 1: Jenkins Setup

<br>

![Jenkins Pipeline](./images/Jenkins_Pipeline.png)

### Launch Jenkins Container:

<br>

**Run:**

```bash
cd Jenkins
docker-compose up -d
```

❗ This Jenkins Docker Container includes all the neccessary tools to run the pipeline.

<br>

- Wait for Jenkins to be accessible
- Login at `http://localhost:8080`
- Copy-Paste `./Jenkins/Jenkinsfile` content into the Jenkins Dashboard -> New Job -> Pipeline

<br><br>

## Step 2: SonarQube Setup

SonarQube Code Quality is used for static code analysis in the CI/CD pipeline.

### Launch SonarQube Container:

**Run:**

```bash
cd SonarQube
docker-compose up -d
```

### 🚀 Access:

- Wait for SonarQube to be accessible at: http://localhost:9000

Default login:
- Username: `admin`
- Password: `admin`

<br>

### 📦 Project Setup:

#### 1. Create new project manually:
- Name it `vprofile`

<br>

<p align="left">
  <img src="./images/SonarQube_Create_New_Project.png" alt="SonarQube - Create New Project" width="500">
</p>

<br>

#### 2. Configure a webhook for the jenkins pipeline:
- Set this URL: `http://host.docker.internal:8080/sonarqube-webhook/`

<br>

<p align="left">
  <img src="./images/SonarQube_Configure_Webhook.png" alt="SonarQube - Configure Webhook" width="1000">
</p>

<br>

### 🔐 Jenkins Integration:

#### 1. Add Sonar Token in Jenkins:
- Manage Jenkins → Credentials
- Kind: Secret Text
- ID: `sonarqube-token`

<br>

<p align="left">
  <img src="./images/Jenkins_SonarQube_C.png" alt="Jenkins SonarQube Creds" width="800">
</p>

<br>

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

<br><br><br>

## Step 3: AWS Configurations:

1. Create ECR Repository name it vprofile/app:

<br>

  You can use the terraform-ecr or manually from AWS Console

**Run:**

```bash
cd terraform-ecr
terraform init
terraform apply
```

<br>

2. Create IAM User with broad permissions policies:

- AmazonEC2ContainerRegistryFullAccess
- AmazonEC2FullAccess
- AmazonECS_FullAccess
- AmazonElastiCacheFullAccess
- AmazonMQFullAccess
- AmazonOpenSearchServiceFullAccess
- AmazonRDSFullAccess
- CloudWatchLogsFullAccess
- IAMFullAccess

Or with [AWS Less Privilege](AWS_Less_Privilege.md) Policies.

<br>

3. Create Access keys:
  - Copy the Access Key and Secret Key into Jenkins Credencials:
    - ID: aws-credentials
    - Access Key ID: AWS Access Key ID
    - Secret Access Key: AWS Secret Key

<br><br><br>

## Step 4: AWS Infrastructure Setup with Terraform

Terraform provisions:

- VPC (2 Public, 2 Private Subnets)
- ALB
- ECS Cluster (Fargate)
- RDS MySQL
- ElastiCache (Memcached)
- RabbitMQ (Amazon MQ)
- OpenSearch
- Security Groups & IAM roles

<br>

**Run:**
```bash
cd terraform
terraform init
terraform apply
```
<br>

⚠️ **Attention:** After a successful deployment, Terraform will output several values that should be added to the Jenkinsfile:

- [x] private_subnets
- [x] ecs_security_group
- [ ] web_url
- [ ] db_endpoint
- [ ] bastion_id

<br>

### **Configure the Jenkinsfile:**

The ECS deployment stage in Jenkins requires the **subnet IDs** and **ECS security group** created by Terraform.

Copy the values of:

- `private_subnets`
- `ecs_security_group`

and update the following variables in the Jenkinsfile:

```groovy
environment {

    AWS_REGION = "us-east-1"

    ECR_REGISTRY = "xxxxxxxxxxxx.dkr.ecr.us-east-1.amazonaws.com"
    ECR_REPO     = "vprofile/app"
    IMAGE_TAG    = "${BUILD_NUMBER}"

    PRIVATE_SUBNETS   = "subnet-xxxxxxxx,subnet-xxxxxxxx"
    ECS_SECURITY_GROUP = "sg-xxxxxxxx"
}
```

❗ These values are used during the ECS deployment process to launch application tasks in the private subnets and attach the correct security group.

<br>
