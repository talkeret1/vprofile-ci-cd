resource "aws_ecs_service" "vproapp_service" {
  name            = "vproapp-service"
  cluster         = aws_ecs_cluster.vprofile_cluster.id
  task_definition = aws_ecs_task_definition.vproapp_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  network_configuration {
    subnets = [
      aws_subnet.vprofile_private_subnet_1.id,
      aws_subnet.vprofile_private_subnet_2.id
    ]

    assign_public_ip = false

    security_groups = [
      aws_security_group.vprofile_ecs_sg.id
    ]
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.vproapp_tg.arn
    container_name   = "vproapp"
    container_port   = 8080
  }
}