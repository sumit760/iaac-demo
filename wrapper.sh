#!/bin/bash


if [ -f "/var/tmp/publicip" ];then
rm -f /var/tmp/publicip
fi

echo "Deploying Demo Instance"

cd infrastructure
terraform destroy -force
terraform init
terraform plan -out demo.plan
terraform apply -auto-approve "demo.plan"
terraform output public_ip > /var/tmp/publicip
echo "Instance Created Successfully"
