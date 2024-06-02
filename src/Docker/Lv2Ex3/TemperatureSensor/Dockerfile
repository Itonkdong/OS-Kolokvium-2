FROM eclipse-temurin
WORKDIR /app/temperature
WORKDIR /app
COPY TemperatureSensor.java .
RUN javac TemperatureSensor.java
CMD ["java", "TemperatureSensor"]
