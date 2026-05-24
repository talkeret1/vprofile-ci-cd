# vprofile App variables

variable "app_port" {
  type        = number
  default     = 8080
  description = "vprofile App port"
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

# Bastion variables

variable "bastion_ami_id" {
  type        = string
  default     = "ami-0236922087fa98b6e" # Amazon Linux 2023 (us-east-1)
  description = "AMI for bastion host"
}

# variable "memcached_host" {
#   type = string
#   # default = ""
#   description = "Memcached host"
# }

# variable "rabbitmq_host" {
#   type = string
#   #default = ""
#   description = "RabbitMQ host"
# }

# variable "rabbitmq_user" {
#   type        = string
#   default     = "guest"
#   description = "RabbitMQ user"
# }

# variable "rabbitmq_pass" {
#   type        = string
#   default     = "guest"
#   description = "RabbitMQ password"
# }
