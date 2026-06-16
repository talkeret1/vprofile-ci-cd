### Step 1: Launch Jenkins:

```bash
cd Jenkins
docker-compose up -d
```
- Wait for Jenkins to be accessible at `http://localhost:8080`
- Read the [Jenkins README](Jenkins/README.md)

### Step 2: Launch SonarQube:

```bash
cd SonarQube
docker-compose up -d
```
- Wait for SonarQube to be accessible at `http://localhost:9000`
- Read the [SonarQube README](SonarQube/README.md)

### Step 3: AWS Configurations:

- Create manually from AWS Console name it vprofile/app
- Create IAM User add policies 
  - AmazonEC2ContainerRegistryFullAccess
  - AmazonECS_FullAccess
- Create Access keys:
  - Copy the Access Key and Secret Key into Jenkins Credencials:
    - ID: aws-credentials
    - Access Key ID: AWS Access Key ID
    - Secret Access Key: AWS Secret Key

### Option 3: Infrastructure with Terraform

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

❗ After a successful deployment, Terraform will output several values. Configure them in the Jenkinsfile:

- [x] private_subnets
- [x] ecs_security_group
- [ ] web_url
- [ ] db_endpoint
- [ ] bastion_id

<br>

**Configure Jenkins:**

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

These values are used during the ECS deployment process to launch application tasks in the private subnets and attach the correct security group.

<br>
