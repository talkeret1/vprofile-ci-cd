resource "aws_ecs_cluster" "vprofile_ecs_cluster" {
  name = "vprofile-ecs-cluster"
}

resource "aws_ecs_service" "vprofile_ecs_service" {
  name            = "vprofile-ecs-service"
  cluster         = aws_ecs_cluster.vprofile_ecs_cluster.id
  task_definition = aws_ecs_task_definition.vproapp_task.arn
  launch_type     = "FARGATE"
  desired_count   = var.ecs_desired_count

  health_check_grace_period_seconds = 120

  network_configuration {
    subnets = [
      aws_subnet.vprofile_private_subnet_1.id,
      aws_subnet.vprofile_private_subnet_2.id
    ]

    assign_public_ip = false

    security_groups = [aws_security_group.vprofile_ecs_sg.id]
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.vproapp_tg.arn
    container_name   = "vproapp"
    container_port   = var.app_port
  }
}

resource "aws_cloudwatch_log_group" "vprofile_ecs_logs" {
  name              = "/ecs/vprofile"
  retention_in_days = 7
}
