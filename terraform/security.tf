resource "aws_security_group" "vprofile_ecs_sg" {
  name        = "vprofile-ecs-sg"
  description = "Allow HTTP access to ECS tasks"
  vpc_id      = aws_vpc.vprofile_vpc.id

  ingress {
    description     = "Allow app access"
    from_port       = var.app_port
    to_port         = var.app_port
    protocol        = "tcp"
    security_groups = [aws_security_group.vprofile_alb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "vprofile-ecs-sg"
  }
}

resource "aws_security_group" "vprofile_alb_sg" {
  name        = "vprofile-alb-sg"
  description = "Allow HTTP traffic"
  vpc_id      = aws_vpc.vprofile_vpc.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "vprofile-alb-sg"
  }
}

resource "aws_security_group" "vprofile_db_sg" {
  name        = "vprofile-db-sg"
  description = "Allow MySQL access only from ECS"
  vpc_id      = aws_vpc.vprofile_vpc.id

  ingress {
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.vprofile_ecs_sg.id, aws_security_group.vprofile_bastion_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "vprofile-db-sg"
  }
}

resource "aws_security_group" "vprofile_memcached_sg" {
  name        = "vprofile-memcached-sg"
  description = "Allow Memcached access only from ECS"
  vpc_id      = aws_vpc.vprofile_vpc.id

  ingress {
    from_port       = 11211
    to_port         = 11211
    protocol        = "tcp"
    security_groups = [aws_security_group.vprofile_ecs_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "vprofile-memcached-sg"
  }
}

resource "aws_security_group" "vprofile_mq_sg" {
  name        = "vprofile-mq-sg"
  description = "Allow RabbitMQ access only from ECS"
  vpc_id      = aws_vpc.vprofile_vpc.id

  ingress {
    from_port       = 5671
    to_port         = 5671
    protocol        = "tcp"
    security_groups = [aws_security_group.vprofile_ecs_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "vprofile-rabbitmq-sg"
  }
}

resource "aws_security_group" "vprofile_opensearch_sg" {
  name        = "vprofile-opensearch-sg"
  description = "Allow OpenSearch access"
  vpc_id      = aws_vpc.vprofile_vpc.id

  ingress {
    from_port       = 443
    to_port         = 443
    protocol        = "tcp"
    security_groups = [aws_security_group.vprofile_ecs_sg.id]
  }
}

resource "aws_security_group" "vprofile_bastion_sg" {
  name        = "vprofile-bastion-sg"
  description = "Allow SSH access to bastion host"
  vpc_id      = aws_vpc.vprofile_vpc.id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "vprofile-bastion-sg"
  }
}
