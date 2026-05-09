FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x gradlew

RUN ./gradlew build --no-daemon

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8098} -jar build/libs/*.jar"]
