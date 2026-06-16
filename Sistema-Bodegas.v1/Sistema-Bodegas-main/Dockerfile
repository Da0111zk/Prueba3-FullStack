FROM eclipse-temurin:17-jre

WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar


ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]


