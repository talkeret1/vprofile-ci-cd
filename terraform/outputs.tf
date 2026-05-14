output "web_url" {
  value = aws_lb.vprofile_alb.dns_name
}

output "db_endpoint" {
  value = aws_db_instance.vprofile_db.address
}