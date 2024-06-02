FROM eclipse-temurin
WORKDIR /app/dataIn
WORKDIR /app/dataOut
WORKDIR /app
COPY SoundLevelMonitor.java .
RUN javac SoundLevelMonitor.java
CMD ["java", "SoundLevelMonitor"]