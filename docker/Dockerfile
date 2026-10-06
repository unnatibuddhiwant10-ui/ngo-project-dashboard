# ========================================================
# Multi-Stage Dockerfile for NGO Project Progress Dashboard
# Stage 1: Build & Package with Maven
# Stage 2: Minimal Distroless / JRE 21 Runtime
# ========================================================

# --- Stage 1: Build ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and compile package
COPY src ./src
RUN mvn clean package -DskipTests

# --- Stage 2: Production Runtime ---
FROM eclipse-temurin:21-jre-jammy
LABEL maintainer="NGO DevOps Team <devops@ngo.org>"
LABEL description="NGO Project Progress Dashboard Container"
LABEL version="1.0.0"

WORKDIR /app

# Create a non-root dedicated application user
RUN groupadd -r spring && useradd -r -g spring spring

# Copy compiled jar from build stage
COPY --from=builder /build/target/ngo-project-dashboard-1.0.0-SNAPSHOT.jar /app/app.jar

# Set permissions
RUN chown -R spring:spring /app

USER spring:spring

# Expose standard Spring Boot HTTP port
EXPOSE 8080

# Environment configurations
ENV PORT=8080 \
    DEPLOY_ENV=production \
    JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# Healthcheck configuration
HEALTHCHECK --interval=30s --timeout=5s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
