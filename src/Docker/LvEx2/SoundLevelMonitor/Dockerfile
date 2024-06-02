FROM eclipse-temurin
WORKDIR /app/data
WORKDIR /app
COPY SoundLevelMonitor.java .
RUN javac SoundLevelMonitor.java
CMD ["java", "SoundLevelMonitor"]