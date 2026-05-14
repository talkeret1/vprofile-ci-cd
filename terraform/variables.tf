variable "db_url" {
    type = string
    # default = ""
    description = "RDS url"
}

variable "db_user" {
    type = string
    default = "admin_vp"
    description = "RDS username"
}

variable "db_pass" {
    type = string
    default = "admin_vp"
    description = "RDS password"
}

variable "memcached_host" {
    type = string
    # default = ""
    description = "Memcached host"
}

variable "rabbitmq_host" {
    type = string
    #default = ""
    description = "RabbitMQ host"
}

variable "rabbitmq_user" {
    type = string
    default = "guest"
    description = "RabbitMQ user"
}

variable "rabbitmq_pass" {
    type = string
    default = "guest"
    description = "RabbitMQ password"
}