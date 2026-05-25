resource "aws_mq_broker" "vprofile_rabbitmq" {
  broker_name = "vprofile-rabbitmq"

  engine_type        = "RabbitMQ"
  engine_version     = "3.13"
  host_instance_type = "mq.m7g.medium"

  deployment_mode = "SINGLE_INSTANCE"

  publicly_accessible = false

  auto_minor_version_upgrade = true

  subnet_ids = [aws_subnet.vprofile_private_subnet_1.id]

  security_groups = [aws_security_group.vprofile_mq_sg.id]

  user {
    username = var.rabbitmq_user
    password = var.rabbitmq_pass
  }

  tags = {
    Name = "vprofile-rabbitmq"
  }
}
