resource "aws_ecs_cluster" "vprofile_cluster" {
  name = "vprofile-cluster"
}

resource "aws_cloudwatch_log_group" "ecs_logs" {
  name              = "/ecs/vprofile"
  retention_in_days = 7
}