FROM eclipse-temurin
WORKDIR /app
WORKDIR /app/data
WORKDIR /app
COPY ExecutionCounter.java .
RUN javac ExecutionCounter.java
CMD ["java", "ExecutionCounter"]

#  docker run -it --rm -e OUT_FILE=ref.out -v .\data:/app/data --name ex3-c ex3 - how to create a mapped volume, on windows u should use \ instead of / for paths LOLOLOLOLO
