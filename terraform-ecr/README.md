# Terraform ECR Bootstrap

## 📌 Overview

This Terraform module provisions an Amazon Elastic Container Registry (ECR) repository used by the VProfile CI/CD pipeline. It serves as the initial bootstrap component required for container image storage before deploying the full AWS infrastructure.

<br>

## 🎯 Purpose

The ECR repository is a prerequisite for the CI/CD pipeline. It stores Docker images built by Jenkins and is referenced by the ECS task definition during deployment.

This module must be applied before the main Terraform infrastructure.

<br>

## 📦 Resources Created

- Amazon ECR Repository (`vprofile/app`)

<br>

## 🚀 Deployment

Initialize and apply the Terraform configuration:

```bash
cd terraform-ecr
terraform init
terraform plan
terraform apply
```

<br>

## 📤 Outputs

The following output is generated after successful deployment:

- `ecr_registry`

This value is used by the Jenkins pipeline to push and deploy Docker images.

<br>

## ⚙️ CI/CD Dependency

This module is required as the first step in the deployment lifecycle:

1. Create ECR repository (this module)
2. Push initial Docker image (Jenkins bootstrap stage)
3. Deploy AWS infrastructure (main Terraform configuration)
4. Continuous deployment via Jenkins pipeline

<br>

## 📌 Notes

- This module is a bootstrap dependency only and does not provision any application infrastructure.
- The repository is used exclusively for storing versioned Docker images for ECS deployments.

<br>

---

⬅️ [Back to README](../README.md)