resource "aws_key_pair" "vprofile_key" {
  key_name   = "vprofile_key"
  public_key = file("/Users/talkeret/.ssh/mysql.pub")
}

resource "aws_security_group" "vprofile_debug_sg" {
  name        = "vprofile-debug-sg"
  description = "Allow SSH access"
  vpc_id      = aws_vpc.vprofile_vpc.id

  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"

    # עדיף להחליף ל-IP שלך
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "vprofile-debug-sg"
  }
}

resource "aws_instance" "vprofile_debug_ec2" {
  ami                    = "ami-0c02fb55956c7d316" # Amazon Linux 2 us-east-1
  instance_type          = "t2.micro"
  subnet_id              = aws_subnet.vprofile_public_subnet_1.id
  vpc_security_group_ids = [aws_security_group.vprofile_debug_sg.id]
  key_name               = aws_key_pair.vprofile_key.key_name

  associate_public_ip_address = true

  tags = {
    Name = "vprofile-debug-ec2"
  }
}
