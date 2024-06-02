FROM eclipse-temurin
WORKDIR /app/dataOut
WORKDIR /app
COPY SoundLevelSensor.java .
RUN javac SoundLevelSensor.java
CMD ["java", "SoundLevelSensor"]