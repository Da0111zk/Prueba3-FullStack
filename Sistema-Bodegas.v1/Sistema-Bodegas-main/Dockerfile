FROM eclipse-temurin:21-jre
WORKDIR /app

USER root
RUN mkdir -p /etc/secrets

COPY --from=build /app/target/*.jar app.jar

RUN echo '#!/bin/sh\n\
echo "[INIT] Inicializando entorno para Oracle Wallet..."\n\
\n\
if [ ! -z "$WALLET_BASE64" ]; then\n\
    echo "$WALLET_BASE64" | base64 -d > /etc/secrets/cwallet.sso\n\
    echo "[INIT] cwallet.sso generado con éxito."\n\
fi\n\
\n\
if [ ! -z "$ORACLE_TNSNAMES" ]; then\n\
    echo "$ORACLE_TNSNAMES" > /etc/secrets/tnsnames.ora\n\
    echo "[INIT] tnsnames.ora generado con éxito."\n\
fi\n\
\n\
if [ ! -z "$ORACLE_SQLNET" ]; then\n\
    echo "$ORACLE_SQLNET" > /etc/secrets/sqlnet.ora\n\
    echo "[INIT] sqlnet.ora generado con éxito."\n\
fi\n\
\n\
exec java $JAVA_OPTS -jar app.jar\n\
' > /app/entrypoint.sh

RUN chmod +x /app/entrypoint.sh
EXPOSE 8080
ENTRYPOINT ["/app/entrypoint.sh"]