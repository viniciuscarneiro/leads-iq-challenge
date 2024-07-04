# Leads Iq challenge

This project is a Java-based backend application  that pulls daily Stock Market Prices for the top 10 S&P 500 companies and displays that data in a
dashboard with Weekly Averages.
The application is built using Spring Boot and incorporates various technologies to ensure scalability, performance, and maintainability.

## Technologies Used

- Java 21
- Spring Boot 3.3.1
- Spring Web
- Spring Data JPA
- Open Feign
- Lombok
- Flyway for database migration
- MySQL Connector
- Docker for containerization

## Running the Application with Docker

To run the application using Docker, follow these steps:

1. Clone the repository to your local machine:

   ```shell
   git clone <repository-url>
   ```

2. Navigate to the project directory:

   ```shell
   cd <project-directory>
   ```

3. Create a `.env` file in the project directory with the following environment variables:

   ```plaintext
   MYSQLDB_USER=<your-mysql-username>
   MYSQLDB_PASSWORD=<your-mysql-password>
   MYSQLDB_DATABASE=<your-mysql-database-name>
   MYSQLDB_LOCAL_PORT=<your-local-mysql-port>
   MYSQLDB_DOCKER_PORT=<your-docker-mysql-port>
   SPRING_LOCAL_PORT=<your-local-spring-port>
   SPRING_DOCKER_PORT=<your-docker-spring-port>
   ALPHA_VANTAGE_API_KEY=MLLBJSSE690AHVOQ
   ```

4. Run the following Docker Compose command to start the MySQL, Redis, and the application containers:

   ```shell
   docker-compose up --build
   ```

5. The application will start, and you can access it at `http://localhost:<your-local-spring-port>`.

## API Endpoints

The application exposes the following REST endpoints:

- `/api/db_create`: Post endpoint to trigger creation of database and schemas.
- `/api/collect_data`: Post endpoint to trigger the collection and storage of stock data.

## Frontend

Overview Page
- `/overview`: Page with week selector and table with stock data.