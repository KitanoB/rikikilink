# Step 1 : Build application
FROM eclipse-temurin:21 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Step 2 : Minimal runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app

# 🔧 Créer le dossier de logs
RUN mkdir -p /app/logs

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]