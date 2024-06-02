FROM eclipse-temurin
WORKDIR /app/data
WORKDIR /app
COPY SoundLevelSensor.java .
RUN javac SoundLevelSensor.java
CMD ["java", "SoundLevelSensor"]