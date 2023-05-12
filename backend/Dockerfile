# syntax=docker/dockerfile:1
FROM hseeberger/scala-sbt:11.0.13_1.5.5_2.13.6 AS builder

WORKDIR /app

COPY ./ /app

RUN sbt assembly

FROM openjdk:11-slim

EXPOSE 8080

COPY --from=builder /app/target/scala-2.13/vend.jar /app/vend.jar

CMD ["java", "-jar", "/app/vend.jar"]
