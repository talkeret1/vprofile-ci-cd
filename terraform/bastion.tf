resource "aws_instance" "vprofile_bastion" {
  ami                         = var.bastion_ami_id
  instance_type               = "t3.micro"
  subnet_id                   = aws_subnet.vprofile_public_subnet_1.id
  associate_public_ip_address = true

  iam_instance_profile = aws_iam_instance_profile.ssm_profile.name

  user_data = templatefile("${path.module}/scripts/init-db.sh", {
    DB_HOST = aws_db_instance.vprofile_db.address
    DB_USER = var.db_user
    DB_PASS = var.db_pass
    DB_NAME = var.db_name
  })

  tags = {
    Name = "vprofile-bastion"
  }
}
