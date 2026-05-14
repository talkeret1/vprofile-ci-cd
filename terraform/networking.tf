resource "aws_vpc" "vprofile_vpc" {
  cidr_block = "10.0.0.0/16"

  enable_dns_support   = true
  enable_dns_hostnames = true

  tags = {
    Name = "vprofile-vpc"
  }
}

resource "aws_internet_gateway" "vprofile_igw" {
  vpc_id = aws_vpc.vprofile_vpc.id

  tags = {
    Name = "vprofile-igw"
  }
}

resource "aws_route_table" "vprofile_public_rt" {
  vpc_id = aws_vpc.vprofile_vpc.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.vprofile_igw.id
  }

  tags = {
    Name = "vprofile-public-rt"
  }
}

resource "aws_subnet" "vprofile_public_subnet_1" {
  vpc_id                  = aws_vpc.vprofile_vpc.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "us-east-1a"
  map_public_ip_on_launch = true

  tags = {
    Name = "vprofile-public-subnet-1"
  }
}

resource "aws_subnet" "vprofile_public_subnet_2" {
  vpc_id                  = aws_vpc.vprofile_vpc.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "us-east-1b"
  map_public_ip_on_launch = true

  tags = {
    Name = "vprofile-public-subnet-2"
  }
}

resource "aws_subnet" "vprofile_private_subnet_1" {
  vpc_id            = aws_vpc.vprofile_vpc.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "us-east-1a"

  tags = {
    Name = "vprofile-private-subnet-1"
  }
}

resource "aws_subnet" "vprofile_private_subnet_2" {
  vpc_id            = aws_vpc.vprofile_vpc.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "us-east-1b"

  tags = {
    Name = "vprofile-private-subnet-2"
  }
}

resource "aws_route_table_association" "vprofile_public_assoc_1" {
  subnet_id      = aws_subnet.vprofile_public_subnet_1.id
  route_table_id = aws_route_table.vprofile_public_rt.id
}

resource "aws_route_table_association" "vprofile_public_assoc_2" {
  subnet_id      = aws_subnet.vprofile_public_subnet_2.id
  route_table_id = aws_route_table.vprofile_public_rt.id
}

resource "aws_eip" "vprofile_nat_eip" {
  domain = "vpc"
}

resource "aws_nat_gateway" "vprofile_nat_gw" {
  allocation_id = aws_eip.vprofile_nat_eip.id
  subnet_id     = aws_subnet.vprofile_public_subnet_1.id

  tags = {
    Name = "vprofile-nat-gw"
  }

  depends_on = [aws_internet_gateway.vprofile_igw]
}

resource "aws_route_table" "vprofile_private_rt" {
  vpc_id = aws_vpc.vprofile_vpc.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.vprofile_nat_gw.id
  }

  tags = {
    Name = "vprofile-private-rt"
  }
}

resource "aws_route_table_association" "vprofile_private_assoc_1" {
  subnet_id      = aws_subnet.vprofile_private_subnet_1.id
  route_table_id = aws_route_table.vprofile_private_rt.id
}

resource "aws_route_table_association" "vprofile_private_assoc_2" {
  subnet_id      = aws_subnet.vprofile_private_subnet_2.id
  route_table_id = aws_route_table.vprofile_private_rt.id
}

