resource "aws_ecs_task_definition" "vproapp_task" {
  family                   = "vproapp-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
  task_role_arn            = aws_iam_role.ecs_task_role.arn

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
          name  = "DB_HOST_NAME"
          value = aws_db_instance.vprofile_db.address
        },
        {
          name  = "DB_HOST_PORT"
          value = "3306"
        },
        {
          name  = "DB_USER"
          value = var.db_user
        },
        {
          name  = "DB_PASS"
          value = var.db_pass
        },
        {
          name  = "DB_NAME"
          value = var.db_name
        },
        {
          name  = "MEMCACHED_HOST_NAME"
          value = aws_elasticache_cluster.vprofile_memcached.cache_nodes[0].address
        },
        {
          name  = "MEMCACHED_HOST_PORT"
          value = "11211"
        },
        {
          name  = "RABBITMQ_HOST_NAME"
          value = split(":", replace(aws_mq_broker.vprofile_rabbitmq.instances[0].endpoints[0], "amqps://", ""))[0]
        },
        {
          name  = "RABBITMQ_HOST_PORT"
          value = "5671"
        },
        {
          name  = "RABBITMQ_USER"
          value = var.rabbitmq_user
        },
        {
          name  = "RABBITMQ_PASS"
          value = var.rabbitmq_pass
        },
        {
          name  = "RABBITMQ_SSL"
          value = "true"
        },
        {
          name  = "RABBITMQ_VHOST"
          value = "/"
        },
        {
          name  = "ELASTICSEARCH_HOST_NAME"
          value = aws_opensearch_domain.vprofile_opensearch.endpoint
        },
        {
          name  = "ELASTICSEARCH_HOST_PORT"
          value = "443"
        },
        {
          name  = "ELASTICSEARCH_SCHEME"
          value = "https"
        }
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
