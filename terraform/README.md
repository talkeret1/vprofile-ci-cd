# Terraform Infrastructure (AWS)

## 📌 Overview

This Terraform setup provisions full cloud infrastructure for the VProfile application.

---

## 🏗️ Resources Created

### Networking
- VPC
- Subnets
- Security Groups
- Routing

---

### Compute
- ECS Cluster (Fargate)
- ECS Service
- Task Definitions

---

### Database
- RDS MySQL

---

### Caching
- ElastiCache (Memcached)

---

### Messaging
- Amazon MQ (RabbitMQ)

---

### Search
- OpenSearch domain

---

## 🚀 Deployment

```bash
cd terraform
terraform init
terraform plan
terraform apply
```

⚙️ Key Files
- networking.tf
- ecs.tf
- ecs-task.tf
- ecs-service.tf
- rds.tf
- memcached.tf
- rabbitmq.tf
- opensearch.tf


🔐 IAM Roles

Terraform creates:

- ECS Task Execution Role
- ECS Task Role

📸 Screenshots
- screenshots/terraform-init.png
- screenshots/terraform-apply.png
- screenshots/aws-console-ecs.png

🧠 Notes

All services are deployed inside a shared VPC for internal communication.