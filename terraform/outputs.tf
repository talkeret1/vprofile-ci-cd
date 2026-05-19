output "web_url" {
  value = aws_lb.vprofile_alb.dns_name
}

output "db_endpoint" {
  value = aws_db_instance.vprofile_db.address
}

output "debug_ec2_public_ip" {
  value = aws_instance.vprofile_debug_ec2.public_ip
}
