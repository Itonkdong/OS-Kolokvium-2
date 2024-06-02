FROM eclipse-temurin
WORKDIR /app
RUN mkdir "data"
COPY LogServer.java .
RUN javac LogServer.java
CMD ["java", "LogServer"]