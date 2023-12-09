FROM alpine:3.16.0
RUN apk add --no-cache openjdk17
COPY build/libs/user-management-ms-0.0.1-SNAPSHOT-plain.jar /app/
WORKDIR /app/
ENTRYPOINT ["java"]
CMD ["-jar", "/app/user-management-ms-0.0.1-SNAPSHOT-plain.jar"]
