# App variables

variable "app_port" {
  type        = number
  default     = 8080
  description = "vprofile App host port"
}

variable "ecs_desired_count" {
  type        = number
  default     = 0
  description = "Number of vprofile App instances"
}


# RDS variables

variable "db_user" {
  type        = string
  default     = "admin_vp"
  description = "RDS username"
}

variable "db_pass" {
  type        = string
  default     = "admin_vp"
  description = "RDS password"
}

variable "db_name" {
  type        = string
  default     = "accounts"
  description = "RDS database name"
}

# RabbitMQ variables

variable "rabbitmq_user" {
  type        = string
  default     = "rabbit"
  description = "RabbitMQ username"
}

variable "rabbitmq_pass" {
  type        = string
  default     = "rabbitrabbit!"
  description = "RabbitMQ password"
}

# Bastion variables

variable "bastion_ami_id" {
  type        = string
  default     = "ami-0236922087fa98b6e" # Amazon Linux 2023 (us-east-1)
  description = "AMI for bastion host"
}
