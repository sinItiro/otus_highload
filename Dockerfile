FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
#Сборка приложения
COPY / /app
WORKDIR /app
RUN mvn package
#Запуск
FROM bellsoft/liberica-runtime-container:jdk-21-slim-musl
ENV PATH=$PATH:/opt/jdk/bin
COPY --from=build  /app/target/network-0.0.1.jar /app/
EXPOSE 8080
CMD ["java", "-showversion", "-jar", "/app/network-0.0.1.jar"]