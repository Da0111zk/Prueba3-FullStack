FROM eclipse-temurin:21-jre
WORKDIR /app

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar


ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Doracle.net.tns_admin=/wallet -jar /app/app.jar"]