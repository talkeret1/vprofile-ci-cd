### Policy for Jenkins:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "ECRPush",
      "Effect": "Allow",
      "Action": [
        "ecr:GetAuthorizationToken",
        "ecr:BatchCheckLayerAvailability",
        "ecr:BatchGetImage",
        "ecr:DescribeImages",
        "ecr:GetDownloadUrlForLayer",
        "ecr:InitiateLayerUpload",
        "ecr:UploadLayerPart",
        "ecr:CompleteLayerUpload",
        "ecr:PutImage"
      ],
      "Resource": "*"
    },
    {
      "Sid": "ECSDeployment",
      "Effect": "Allow",
      "Action": [
        "ecs:DescribeServices",
        "ecs:UpdateService"
      ],
      "Resource": "*"
    }
  ]
}
```

<br>

### Policy for Terraform-ECR:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "TerraformECRRepository",
      "Effect": "Allow",
      "Action": [
        "ecr:CreateRepository",
        "ecr:DeleteRepository",
        "ecr:DescribeRepositories",
        "ecr:ListTagsForResource"
      ],
      "Resource": "*"
    }
  ]
}
```

<br>

### Policy for Terraform CI/CD:

```json
{
	"Version": "2012-10-17",
	"Statement": [
		{
			"Sid": "IAM",
			"Effect": "Allow",
			"Action": [
				"iam:AddRoleToInstanceProfile",
				"iam:AttachRolePolicy",
				"iam:CreateInstanceProfile",
				"iam:CreateRole",
				"iam:DeleteInstanceProfile",
				"iam:DeleteRole",
				"iam:DetachRolePolicy",
				"iam:GetInstanceProfile",
				"iam:GetRole",
				"iam:ListAttachedRolePolicies",
				"iam:ListInstanceProfilesForRole",
				"iam:ListRolePolicies",
				"iam:PassRole",
				"iam:PutRolePolicy",
				"iam:DeleteRolePolicy",
				"iam:RemoveRoleFromInstanceProfile",
				"iam:TagRole",
				"iam:UntagRole",
				"iam:ListRoleTags",
				"iam:GetRolePolicy",
				"iam:GetRole"
			],
			"Resource": [
				"arn:aws:iam::*:role/vprofile-*",
				"arn:aws:iam::*:instance-profile/vprofile-*"
			] 
		},
		{
			"Sid": "EC2Networking",
			"Effect": "Allow",
			"Action": [
				"ec2:AllocateAddress",
				"ec2:AssociateAddress",
				"ec2:AssociateRouteTable",
				"ec2:AttachInternetGateway",
				"ec2:AuthorizeSecurityGroupIngress",
				"ec2:AuthorizeSecurityGroupEgress",
				"ec2:RevokeSecurityGroupEgress",
				"ec2:CreateInternetGateway",
				"ec2:CreateNatGateway",
				"ec2:CreateRoute",
				"ec2:CreateRouteTable",
				"ec2:CreateSecurityGroup",
				"ec2:CreateSubnet",
				"ec2:CreateTags",
				"ec2:CreateVpc",
				"ec2:DeleteInternetGateway",
				"ec2:DeleteNatGateway",
				"ec2:DeleteRouteTable",
				"ec2:DeleteSecurityGroup",
				"ec2:DeleteSubnet",
				"ec2:DeleteTags",
				"ec2:DeleteVpc",
				"ec2:DetachInternetGateway",
				"ec2:DisassociateAddress",
				"ec2:DisassociateRouteTable",
				"ec2:ModifySubnetAttribute",
				"ec2:ModifyVpcAttribute",
				"ec2:ReleaseAddress",
				"ec2:DescribeAddresses",
				"ec2:DescribeAddressesAttribute",
				"ec2:DescribeAvailabilityZones",
				"ec2:DescribeImages",
				"ec2:DescribeInstances",
				"ec2:DescribeInstanceTypes",
				"ec2:DescribeInternetGateways",
				"ec2:DescribeNatGateways",
				"ec2:DescribeNetworkInterfaces",
				"ec2:DescribeRouteTables",
				"ec2:DescribeSecurityGroups",
				"ec2:DescribeSubnets",
				"ec2:DescribeTags",
				"ec2:DescribeVpcs",
				"ec2:DescribeVpcAttribute",
				"ec2:DescribeInstanceAttribute",
				"ec2:DescribeVolumes",
				"ec2:DescribeInstanceCreditSpecifications",
				"ec2:DetachNetworkInterface",
				"ec2:DeleteNetworkInterface",
				"ec2:DeleteNetworkInterfacePermission"
			],
			"Resource": "*"
		},
		{
			"Sid": "EC2Instances",
			"Effect": "Allow",
			"Action": [
				"ec2:RunInstances",
				"ec2:TerminateInstances"
			],
			"Resource": "*"
		},
		{
			"Sid": "ECS",
			"Effect": "Allow",
			"Action": [
				"ecs:CreateCluster",
				"ecs:CreateService",
				"ecs:DeleteCluster",
				"ecs:DeleteService",
				"ecs:DeregisterTaskDefinition",
				"ecs:RegisterTaskDefinition",
				"ecs:UpdateService",
				"ecs:DescribeClusters",
				"ecs:DescribeServices",
				"ecs:DescribeTaskDefinition",
				"ecs:DescribeTaskSets",
				"ecs:ListClusters",
				"ecs:ListServices",
				"ecs:ListTaskDefinitions"
			],
			"Resource": "*"
		},
		{
			"Sid": "ELB",
			"Effect": "Allow",
			"Action": [
				"elasticloadbalancing:CreateListener",
				"elasticloadbalancing:CreateLoadBalancer",
				"elasticloadbalancing:CreateTargetGroup",
				"elasticloadbalancing:DeleteListener",
				"elasticloadbalancing:DeleteLoadBalancer",
				"elasticloadbalancing:DeleteTargetGroup",
				"elasticloadbalancing:ModifyLoadBalancerAttributes",
				"elasticloadbalancing:ModifyTargetGroupAttributes",
				"elasticloadbalancing:DescribeTargetGroupAttributes",
				"elasticloadbalancing:DescribeLoadBalancerAttributes",
				"elasticloadbalancing:RegisterTargets",
				"elasticloadbalancing:DeregisterTargets",
				"elasticloadbalancing:AddTags",
				"elasticloadbalancing:RemoveTags",
				"elasticloadbalancing:DescribeListeners",
				"elasticloadbalancing:DescribeLoadBalancers",
				"elasticloadbalancing:DescribeTargetGroups",
				"elasticloadbalancing:DescribeTargetHealth",
				"elasticloadbalancing:DescribeTags"
			],
			"Resource": "*"
		},
		{
			"Sid": "CloudWatchLogs",
			"Effect": "Allow",
			"Action": [
				"logs:CreateLogGroup",
				"logs:DeleteLogGroup",
				"logs:PutRetentionPolicy",
				"logs:DescribeLogGroups",
				"logs:DescribeLogStreams",
				"logs:ListTagsForResource",
				"logs:TagResource",
				"logs:UntagResource"
			],
			"Resource": "*"
		},
		{
			"Sid": "RDS",
			"Effect": "Allow",
			"Action": [
				"rds:CreateDBInstance",
				"rds:DeleteDBInstance",
				"rds:CreateDBSubnetGroup",
				"rds:DeleteDBSubnetGroup",
				"rds:AddTagsToResource",
				"rds:RemoveTagsFromResource",
				"rds:ListTagsForResource",
				"rds:DescribeDBInstances",
				"rds:DescribeDBSubnetGroups"
			],
			"Resource": "*"
		},
		{
			"Sid": "ElastiCache",
			"Effect": "Allow",
			"Action": [
				"elasticache:CreateCacheCluster",
				"elasticache:DeleteCacheCluster",
				"elasticache:CreateCacheSubnetGroup",
				"elasticache:DeleteCacheSubnetGroup",
				"elasticache:DescribeCacheClusters",
				"elasticache:DescribeCacheSubnetGroups",
				"elasticache:ListTagsForResource",
				"elasticloadbalancing:DescribeListenerAttributes"
			],
			"Resource": "*"
		},
		{
			"Sid": "MQ",
			"Effect": "Allow",
			"Action": [
				"mq:CreateBroker",
				"mq:DeleteBroker",
				"mq:DescribeBroker",
				"mq:CreateTags"
			],
			"Resource": "*"
		},
		{
			"Sid": "OpenSearch",
			"Effect": "Allow",
			"Action": [
				"es:CreateDomain",
				"es:DeleteDomain",
				"es:UpdateDomainConfig",
				"es:DescribeDomain",
				"es:DescribeDomainConfig",
				"es:ListTags"
			],
			"Resource": "*"
		},
		{
			"Sid": "ECRRead",
			"Effect": "Allow",
			"Action": [
				"ecr:GetAuthorizationToken",
				"ecr:BatchGetImage",
				"ecr:BatchCheckLayerAvailability",
				"ecr:GetDownloadUrlForLayer"
			],
			"Resource": "*"
		},
		{
			"Sid": "General",
			"Effect": "Allow",
			"Action": [
				"sts:GetCallerIdentity",
				"kms:DescribeKey",
				"kms:Decrypt",
				"kms:GenerateDataKey",
				"kms:CreateGrant",
				"kms:RetireGrant"
			],
			"Resource": "*"
		}
	]
}
```