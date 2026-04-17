# syntax=docker/dockerfile:1
ARG JDK_VERSION=25
ARG MAVEN_VERSION=3.9.14

FROM maven:${MAVEN_VERSION}-eclipse-temurin-${JDK_VERSION} AS builder
WORKDIR /workspace
COPY pom.xml ./
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:${JDK_VERSION}-jre
ARG JDK_VERSION
ARG MAVEN_VERSION
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar
LABEL local.java.version="${JDK_VERSION}"
LABEL local.maven.version="${MAVEN_VERSION}"
EXPOSE 8080
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]
