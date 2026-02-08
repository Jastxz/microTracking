# ---- Runtime ----
FROM amazoncorretto:21-alpine

ARG TRACKING_NOTIFICATION_EMAIL
ARG RESEND_API_KEY
ARG FROM

WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir -p /app/data /app/src/main/resources && chown -R spring:spring /app

COPY target/*.jar /app/app.jar
RUN chown spring:spring /app/app.jar

# Crear el archivo .emailData desde build args
RUN echo "TRACKING_NOTIFICATION_EMAIL: ${TRACKING_NOTIFICATION_EMAIL}" > /app/src/main/resources/.emailData && \
    echo "RESEND_API_KEY: ${RESEND_API_KEY}" >> /app/src/main/resources/.emailData && \
    echo "FROM: ${FROM}" >> /app/src/main/resources/.emailData && \
    chown spring:spring /app/src/main/resources/.emailData

USER spring:spring

EXPOSE 5555
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
