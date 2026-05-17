# variable "db_url" {
#     type = string
#     description = "RDS url"
# }

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

variable "image_tag" {}

# terraform apply -var="image_tag=7"


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