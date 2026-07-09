# Git repository
git_repo = "https://github.com/talkeret1/vprofile-ci-cd.git"

# ECR variables
ecr_registry = "943560362977.dkr.ecr.us-east-1.amazonaws.com"
ecr_repo     = "vprofile/app"

# ECS variables
ecs_desired_count = 1

# App variables
app_port = 8080

# RDS variables
db_user = "admin_vp"
db_pass = "admin_vp"
db_name = "accounts"

# RabbitMQ variables
rabbitmq_user = "rabbit"
rabbitmq_pass = "rabbitrabbit!"

# Bastion variables
bastion_ami_id = "ami-0236922087fa98b6e" # Amazon Linux 2023 (us-east-1)
