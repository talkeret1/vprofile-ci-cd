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
      image = "talkeret/flyway:latest"

      entryPoint = ["flyway"]

      environment = [
        {
          name  = "FLYWAY_URL"
          value = "jdbc:mysql://${aws_db_instance.vprofile_db.address}:3306/accounts?allowPublicKeyRetrieval=true&useSSL=false"
        },
        {
          name  = "FLYWAY_USER"
          value = "admin_vp"
        },
        {
          name  = "FLYWAY_PASSWORD"
          value = "[PASSWORD]"
        }
      ]

      command   = ["migrate"]
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
