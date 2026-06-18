resource "aws_lb" "vprofile_alb" {
  name               = "vprofile-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.vprofile_alb_sg.id]
  subnets = [
    aws_subnet.vprofile_public_subnet_1.id,
    aws_subnet.vprofile_public_subnet_2.id
  ]

  tags = {
    Name = "vprofile-alb"
  }
}

resource "aws_lb_target_group" "vprofile_tg" {
  name        = "vprofile-tg"
  port        = var.app_port
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_vpc.vprofile_vpc.id

  health_check {
    path     = "/"
    protocol = "HTTP"
    matcher  = "200"
  }
}

resource "aws_lb_listener" "vprofile_http_listener" {
  load_balancer_arn = aws_lb.vprofile_alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.vprofile_tg.arn
  }
}
