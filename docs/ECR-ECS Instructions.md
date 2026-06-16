-------------------------
AWS - ECS Configurations:
-------------------------

- Create ECR Repository:
    - Name: vprofile/app
- Create IAM User:
    - Add Policies: 
        - AmazonEC2ContainerRegistryFullAccess
        - AmazonECS_FullAccess
    - Create Access keys:
        - Copy the Access Key and Secret Key into Jenkins Credencials

-----------------------
Jenkins Configurations:
-----------------------

- Manage Jenkins -> Credentials -> AWS Credentials:
    - Copy the Keys from AWS IAM User
        - ID: aws-credentials
        - Access Key ID: AWS Access Key ID
        - Secret Access Key: AWS Secret Key