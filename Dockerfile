FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace/app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q dependency:go-offline

COPY src ./src
RUN ./mvnw -q clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /workspace/app/target/payments-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
