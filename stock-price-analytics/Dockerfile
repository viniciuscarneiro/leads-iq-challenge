# Stage 1: Build the application
FROM gradle:8.7-jdk21 AS builder
WORKDIR /app
COPY . .
RUN gradle build
COPY . .
# Stage 2: Create the final image
FROM alpine:latest as jdk21
RUN apk --no-cache add openjdk21
RUN java -version

# Stage 3: Copy the built application
FROM builder as application
WORKDIR /app
COPY --from=builder /app/build/libs/stock-price-analytics-0.0.1-SNAPSHOT.jar /app/

# Stage 4: Final image
FROM jdk21
WORKDIR /app
COPY --from=application /app/stock-price-analytics-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8080
CMD ["java", "-jar", "stock-price-analytics-0.0.1-SNAPSHOT.jar"]