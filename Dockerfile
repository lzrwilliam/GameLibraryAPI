# Use the official OpenJDK 21 image as the base image
FROM eclipse-temurin:21-jdk

# Set environment variable to configure Java to open debug port 5005
ENV JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=*:5005,server=y,suspend=n

ARG APP_VERSION=0.0.1-SNAPSHOT

COPY ./build/libs/hello-${APP_VERSION}.jar /hello/libs/hello.jar
# Set the working directory inside the image
WORKDIR /hello/libs/

# Define the command to run the application
CMD ["java", "-jar", "/hello/libs/hello.jar"]