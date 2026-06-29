FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

USER root
RUN mkdir -p /etc/secrets

COPY --from=build /app/target/*.jar app.jar

RUN echo '#!/bin/sh\n\
if [ -f /etc/secrets/cwallet.sso ]; then\n\
    echo "[INIT] Detectada cwallet.sso en Base64. Decodificando..."\n\
    base64 -d /etc/secrets/cwallet.sso > /etc/secrets/cwallet.bin\n\
    mv /etc/secrets/cwallet.bin /etc/secrets/cwallet.sso\n\
    echo "[INIT] Billetera decodificada con éxito."\n\
else\n\
    echo "[WARNING] No se encontró cwallet.sso en /etc/secrets/ - Saltando paso."\n\
fi\n\
\n\
exec java $JAVA_OPTS -jar app.jar\n\
' > /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/app/entrypoint.sh"]