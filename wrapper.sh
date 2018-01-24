#!/bin/bash


if [ -f "/var/tmp/publicip" ];then
rm -f /var/tmp/publicip
fi

if [ -f "/var/tmp/web.properties" ];then
  rm -f /var/tmp/web.properties
fi

echo "Deploying Demo Instance"

cd infrastructure
/usr/local/bin/terraform destroy -force
/usr/local/bin/terraform init
/usr/local/bin/terraform plan -out demo.plan
/usr/local/bin/terraform apply -auto-approve "demo.plan"
/usr/local/bin/terraform output public_ip > /var/tmp/publicip
echo "Instance Created Successfully"


echo "Creating App Url"
INSTANCE_IP=`cat /var/tmp/publicip`
echo "URL=http://${INSTANCE_IP}:9080/Presentation-0.0.1-SNAPSHOT" > /var/tmp/web.properties
