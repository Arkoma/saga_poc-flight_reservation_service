FROM eclipse-temurin:11-alpine
RUN mkdir /opt/app
COPY build/libs/saga.saga_poc__flight_reservation_service-0.0.1-SNAPSHOT.jar /opt/app
CMD ["java", "-jar", "/opt/app/saga.saga_poc__flight_reservation_service-0.0.1-SNAPSHOT.jar"]