resource "aws_ecs_task_definition" "flyway_task" {
  family                   = "flyway-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  depends_on               = [aws_db_instance.vprofile_db]

  container_definitions = jsonencode([
    {
      name  = "flyway"
      image = "flyway/flyway:10"

      entryPoint = ["flyway"]

      command = [
        "migrate",
        "-url=jdbc:mysql://${aws_db_instance.vprofile_db.address}:3306/accounts",
        "-user=admin_vp",
        "-password=admin_vp"
      ]

      essential = true

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = "/ecs/vprofile"
          awslogs-region        = "us-east-1"
          awslogs-stream-prefix = "flyway"
        }
      }
    }
  ])
}
