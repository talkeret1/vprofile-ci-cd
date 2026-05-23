resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_ecs_task_definition" "vproapp_task" {
  family                   = "vproapp-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name  = "vproapp"
      image = "943560362977.dkr.ecr.us-east-1.amazonaws.com/vprofile/app:latest"

      portMappings = [{
        containerPort = var.app_port
        protocol      = "tcp"
      }]

      environment = [
        {
          name  = "DB_HOST"
          value = aws_db_instance.vprofile_db.address
        },
        {
          name  = "DB_USER"
          value = var.db_user
        },
        {
          name  = "DB_PASS"
          value = var.db_pass
        },
        # {
        #   name  = "MEMCACHED_HOST"
        #   value = var.memcached_host
        # },
        # {
        #   name  = "RABBITMQ_HOST"
        #   value = var.rabbitmq_host
        # },
        # {
        #   name  = "RABBITMQ_USER"
        #   value = var.rabbitmq_user
        # },
        # {
        #   name  = "RABBITMQ_PASS"
        #   value = var.rabbitmq_pass
        # }
      ],

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = "/ecs/vprofile"
          awslogs-region        = "us-east-1"
          awslogs-stream-prefix = "vproapp"
        }
      }
    }
  ])
}
