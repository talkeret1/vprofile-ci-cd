resource "aws_elasticache_cluster" "vprofile_memcached" {
  cluster_id      = "vprofile-memcached"
  engine          = "memcached"
  node_type       = "cache.t3.micro"
  num_cache_nodes = 1
  port            = 11211

  subnet_group_name  = aws_elasticache_subnet_group.vprofile_memcached_subnets.name
  security_group_ids = [aws_security_group.vprofile_memcached_sg.id]
}

resource "aws_elasticache_subnet_group" "vprofile_memcached_subnets" {
  name = "vprofile-memcached-subnets"

  subnet_ids = [
    aws_subnet.vprofile_private_subnet_1.id,
    aws_subnet.vprofile_private_subnet_2.id
  ]
}
