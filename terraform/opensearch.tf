resource "aws_opensearch_domain" "vprofile_opensearch" {
  domain_name    = "vprofile-opensearch"
  engine_version = "OpenSearch_2.11"

  cluster_config {
    instance_type          = "t3.small.search"
    instance_count         = 1
    zone_awareness_enabled = false
  }

  vpc_options {
    subnet_ids         = [aws_subnet.vprofile_private_subnet_1.id]
    security_group_ids = [aws_security_group.vprofile_opensearch_sg.id]
  }

  ebs_options {
    ebs_enabled = true
    volume_size = 10
    volume_type = "gp3"
  }

  encrypt_at_rest {
    enabled = true
  }

  node_to_node_encryption {
    enabled = true
  }

  domain_endpoint_options {
    enforce_https       = true
    tls_security_policy = "Policy-Min-TLS-1-2-2019-07"
  }

  advanced_security_options {
    enabled                        = false
    internal_user_database_enabled = false
  }

  access_policies = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          AWS = "*"
        },
        Action   = "es:*",
        Resource = "arn:aws:es:us-east-1:943560362977:domain/vprofile-opensearch/*"
      }
    ]
  })

  timeouts {
    create = "60m"
    update = "60m"
    delete = "60m"
  }
}
