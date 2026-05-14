resource "aws_security_group" "vprofile_ecs_sg" {
  name        = "vprofile-ecs-sg"
  description = "Allow HTTP access to ECS tasks"
  vpc_id      = aws_vpc.vprofile_vpc.id

  ingress {
    description     = "Allow app access"
    from_port       = 8080
    to_port         = 8080
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
    security_groups = [aws_security_group.vprofile_ecs_sg.id]
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