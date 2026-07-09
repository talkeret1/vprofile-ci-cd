resource "aws_ecr_repository" "vprofile_ecr" {
  name                 = "vprofile/app"
  image_tag_mutability = "MUTABLE"
  force_delete         = true
}
