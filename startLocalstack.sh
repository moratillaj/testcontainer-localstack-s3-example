
docker run -d -p "4567-4583:4567-4583"                        \
            -p 8080:8080 \
             -e SERVICES=s3                   \
             -e DATA_DIR=/tmp/localstack/data \
             -e PORT_WEB_UI=8080             \
             -e DEBUG=1 \
             --name test-localstack-s3 \
             localstack/localstack

#aws cli
export AWS_ACCESS_KEY_ID=oneaccesskey
export AWS_SECRET_ACCESS_KEY=onesecretkey

aws --endpoint-url=http://localhost:4572 s3 mb s3://mybucket
aws --endpoint-url=http://localhost:4572 s3 ls
aws --endpoint-url=http://localhost:4572 s3 ls s3://mybucket

http://localhost:4572/mybucket
