resource "aws_db_subnet_group" "vprofile_db_subnets" {
  name = "vprofile-db-subnet-group"

  subnet_ids = [
    aws_subnet.vprofile_private_subnet_1.id,
    aws_subnet.vprofile_private_subnet_2.id
  ]

  tags = {
    Name = "vprofile-db-subnets"
  }
}

resource "aws_db_instance" "vprofile_db" {
  identifier = "vprofile-db"

  engine         = "mysql"
  engine_version = "8.0"
  instance_class = "db.t3.micro"

  allocated_storage = 20

  db_name  = var.db_name
  username = var.db_user
  password = var.db_pass

  db_subnet_group_name   = aws_db_subnet_group.vprofile_db_subnets.name
  vpc_security_group_ids = [aws_security_group.vprofile_db_sg.id]

  skip_final_snapshot = true

  publicly_accessible = false

  multi_az = false

  tags = {
    Name = "vprofile-db"
  }
}
