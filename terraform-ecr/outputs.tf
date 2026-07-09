output "ecr_registry" {
  value = split("/", aws_ecr_repository.vprofile_ecr.repository_url)[0]
}

output "ecr_repo" {
  value = "vprofile/app"
}
