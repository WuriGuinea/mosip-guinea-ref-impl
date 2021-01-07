all: package build-image run-image

package:
	mvn clean package

build-image:
	docker build -t ui-redeployer-tester .

run-image:
	docker run -d -e AWSKEY=hello-wolrd ui-redeployer-tester
