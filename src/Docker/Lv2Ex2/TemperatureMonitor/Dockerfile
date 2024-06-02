FROM eclipse-temurin
WORKDIR /app/data
WORKDIR /app
COPY TemperatureMonitor.java .
RUN javac TemperatureMonitor.java
CMD ["java", "TemperatureMonitor"]
