# FROM maven:3.8.5-openjdk-17 AS build
# COPY . .
# RUN mvn clean package -DskipTests
#
# FROM openjdk:17.0.1-jdk-slim
# COPY --from=build /target/apigw-backend-userdetails-0.0.1-SNAPSHOT.jar demo.jar
# EXPOSE 8080
# ENTRYPOINT ["java","-jar","demo.jar"]


# ===========================
# Stage 1: Build the JAR
# ===========================
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ===========================
# Stage 2: Run the JAR
# ===========================
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy the exact built jar
COPY --from=build /app/target/apigw-backend-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
