provider "aws" {
  region  = "ap-south-1"
  profile = "default"
}

resource "aws_instance" "pwc_demo_instances" {
  count = "1"
  instance_type          = "t2.large"
  ami                    = "ami-5b99c834"
  key_name               = "PWC-DEMO-K"
  availability_zone      = "ap-south-1a"
  subnet_id              = "subnet-bed470d6"
}


output "public_ip" {
  value = "${aws_instance.pwc_demo_instances.public_ip}"
}
