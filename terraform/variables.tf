# App variables

variable "app_port" {
  type        = number
  default     = 8080
  description = "vprofile App host port"
}

variable "ecs_desired_count" {
  type        = number
  default     = 1
  description = "Number of vprofile App instances"
}


# RDS variables

variable "db_user" {
  type        = string
  description = "RDS username"
}

variable "db_pass" {
  type        = string
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
  description = "RabbitMQ username"
}

variable "rabbitmq_pass" {
  type = string

  description = "RabbitMQ password"
}

# Bastion variables

variable "bastion_ami_id" {
  type        = string
  description = "AMI for bastion host"
}
