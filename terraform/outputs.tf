output "private_subnets" {
  value = [
    aws_subnet.vprofile_private_subnet_1.id,
    aws_subnet.vprofile_private_subnet_2.id
  ]
}

output "ecs_security_group" {
  value = aws_security_group.vprofile_ecs_sg.id
}

output "web_url" {
  value = aws_lb.vprofile_alb.dns_name
}

output "db_endpoint" {
  value = aws_db_instance.vprofile_db.address
}

output "bastion_id" {
  value = aws_instance.vprofile_bastion.id
}
