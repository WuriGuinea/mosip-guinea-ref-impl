FROM openjdk:8

WORKDIR /app

ENV AWSKEY=${aws_key}

COPY ./target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]



