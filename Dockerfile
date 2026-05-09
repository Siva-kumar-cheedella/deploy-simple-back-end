FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY build/libs/*.jar app.jar

ENTRYPOINT ["sh", "-c", "java -Dserver.port=$PORT -jar app.jar"]
