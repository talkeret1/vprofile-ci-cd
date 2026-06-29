# Setup Instructions

<br>

To Run This Project You need to Setup Jenkins and SonarQube Docker Containers and Configure AWS Environment, Follow the Steps Below:

<br>

## Step 1: Jenkins Setup

<br>

![Jenkins Pipeline](./images/Jenkins_Pipeline.png)

### 1. Launch Jenkins Container:

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

### 2. Launch SonarQube Container:

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

#### 3. Create new project manually:
- Name it `vprofile`

<br>

<p align="left">
  <img src="./images/SonarQube_Create_New_Project.png" alt="SonarQube - Create New Project" width="500">
</p>

<br>

#### 4. Configure a webhook for the jenkins pipeline:

<br>

- Navigate to: **Projects → vprofile → Project Settings → Webhooks**
- Click on **"Create a Webhook"** and configure as follows:
  - Name: `jenkins`
  - URL: `http://host.docker.internal:8080/sonarqube-webhook/`

<br>

<p align="left">
  <img src="./images/SonarQube_Configure_Webhook.png" alt="SonarQube - Configure Webhook" width="1000">
</p>

<br>

#### 5. Generate Token for Jenkins:

<br>

- Navigate to: **Administrator → My Account**
- Click on the **Security** Tab
- **Generate** a Token, name it `vprofile` 
- Copy the Token to use in Jenkins

<br>

<p align="left">
  <img src="./images/SonarQube_Generate_Token.png" alt="SonarQube - Generate Token" width="800">
</p>

<br>

### 🔐 Jenkins Integration:

#### 6. Add Sonar Token in Jenkins:
- Navigate to: **Jenkins → Credentials**
- Credential Type: Secret Text
- ID: `sonarqube-token`

<br>

<p align="left">
  <img src="./images/Jenkins_SonarQube_Credentials.png" alt="Jenkins SonarQube Credentials" width="800">
</p>

<br><br><br>

## Step 3: AWS Configurations:

#### 7. Create **IAM User** with broad permissions policies:

- `AmazonEC2ContainerRegistryFullAccess`
- `AmazonEC2FullAccess`
- `AmazonECS_FullAccess`
- `AmazonElastiCacheFullAccess`
- `AmazonMQFullAccess`
- `AmazonOpenSearchServiceFullAccess`
- `AmazonRDSFullAccess`
- `CloudWatchLogsFullAccess`
- `IAMFullAccess`

❗ Or with [AWS Less Privilege](AWS_Less_Privilege.md) Policies.

<br>

#### 8. Create **Access keys** for the IAM User:
- ID: `aws-credentials`
- Access Key ID: AWS Access Key ID
- Secret Access Key: AWS Secret Key

<br>

### 🔐 Jenkins Integration:

#### 9. Add AWS Credentials in Jenkins:
- Navigate to: **Jenkins → Credentials**
- Credential Type: AWS Credentials
- ID: `aws-credentials`

<br>

<p align="left">
  <img src="./images/Jenkins_AWS_Credentials.png" alt="Jenkins AWS Credentials" width="800">
</p>

<br><br><br>

## Step 4: Create ECR Repository:

<br>

  You can use the **terraform-ecr** or you can create it manually from the AWS Console.
  - Repository name: `vprofile/app`

**Run:**

```bash
cd terraform-ecr
terraform init
terraform apply
```
<br>

<p align="left">
  <img src="./images/Terraform_ECR_Output.png" alt="Terraform ECR Output" width="500">
</p>

<br>

When Apply Complete copy the output value `ecr_registry` and update the **Jenkinsfile**.

<br>

<p align="left">
  <img src="./images/Jenkins_Pipeline_Script.png" alt="Jenkins Pipeline Script" width="1000">
</p>

<br><br><br>

## Step 5: Load Image to ECR:

- The **first pipeline** execution is used as a **bootstrap step**. Jenkins logs in to Amazon ECR and pushes the initial Docker image, allowing the ECS infrastructure to be provisioned. 
- After the **ECS infrastructure is available**, every subsequent pipeline execution pushes a new image to Amazon ECR and deploys it to Amazon ECS.

**Run the jenkins pipeline:**

![Jenkins Bootstrap Pipeline](./images/Jenkins_Bootstrap_Pipeline.png)

<br>

## Step 6: Build the AWS Infrastructure with Terraform:

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
