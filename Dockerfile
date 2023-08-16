# Start with base image
FROM openjdk:17-jdk-alpine

ADD build/libs/rabbits-manager.jar rabbits-manager.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "rabbits-manager.jar"]