FROM eclipse-temurin
WORKDIR /app/data
WORKDIR /app
COPY TemperatureSensor.java .
RUN javac TemperatureSensor.java
CMD ["java", "TemperatureSensor"]
