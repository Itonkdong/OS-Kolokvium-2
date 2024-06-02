FROM eclipse-temurin
WORKDIR /app/temperaturelevel
WORKDIR /app/temperature
WORKDIR /app
COPY TemperatureMonitor.java .
RUN javac TemperatureMonitor.java
CMD ["java", "TemperatureMonitor"]
