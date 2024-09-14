# Car Rental and Fleet Management System

## Overview

The **Car Rental and Fleet Management System** is a comprehensive and scalable solution designed for managing car rentals, fleet monitoring, and user operations in real time. It utilizes a modern tech stack with microservices architecture, allowing seamless scalability, event-driven data processing, and robust user authentication. The system is built for high availability, security, and flexibility, making it adaptable to the needs of small to large rental businesses and fleet operators.

## Key Features

### 1. Real-Time Monitoring and Event Processing
- **Kafka Integration**: The system leverages Apache Kafka to handle real-time event streaming, allowing for continuous monitoring and updates on vehicle status, rental transactions, user activity, and system health.
- **Fleet Monitoring**: Real-time data collection and processing allow for accurate monitoring of the fleet, including vehicle location, condition, and usage patterns. The system ensures that critical events (e.g., vehicle malfunctions, overdue rentals) are captured and addressed promptly.

### 2. Scalable Architecture
- **Microservices-based Architecture**: Built with **Spring Boot**, the application is split into multiple services, each responsible for a specific business function (e.g., user management, vehicle management, rental service).
- **Apache Kafka**: The system uses Kafka for event streaming, ensuring that real-time updates are efficiently processed and distributed across the microservices.
- **Containerization**: Each service is containerized using **Docker** to ensure portability and consistency across different environments.
- **Orchestration**: The services are deployed and managed through **Kubernetes**, providing auto-scaling, load balancing, and service discovery, ensuring high availability.
- **PostgreSQL Database**: A **PostgreSQL** instance is used as the primary database for storing vehicle data, user information, and rental transactions securely.
  
### 3. Managed User Authentication
- The system integrates a secure authentication mechanism for user and admin access. Each user is assigned roles, and access is managed based on roles, ensuring secure management of data and transactions.
  
### 4. Fleet Monitoring
- The system provides a fleet monitoring dashboard for tracking vehicle availability, condition, and performance metrics. The dashboard includes real-time tracking capabilities using event data processed through Kafka.

## Tech Stack

- **Backend**: Spring Boot, Kafka
- **Frontend**: React (optional, for UI integration)
- **Database**: PostgreSQL
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **Authentication**: JWT or OAuth (customizable)
- **Real-Time Processing**: Apache Kafka

## Installation

### Prerequisites
- Docker
- Kubernetes (Minikube or any Kubernetes cluster)
- Java 11 or higher
- PostgreSQL

### Step-by-Step Guide

1. **Clone the repository**:
    ```bash
    git clone https://github.com/jooncybertest/CarInventoryManagement.git
    cd car-rental-fleet-management
    ```

2. **Start the PostgreSQL Database**:
    Make sure to have a running PostgreSQL instance. You can use Docker to start a PostgreSQL container:
    ```bash
    docker run --name fleet-postgres -e POSTGRES_PASSWORD=password -d postgres
    ```

3. **Run the Microservices**:
    Use Maven to build and run each service:
    ```bash
    cd service-name
    mvn clean install
    mvn spring-boot:run
    ```

4. **Set Up Kafka**:
    Kafka can be run using Docker:
    ```bash
    docker-compose up kafka
    ```

5. **Deploy with Kubernetes**:
    Use `kubectl` to deploy the microservices on Kubernetes:
    ```bash
    kubectl apply -f k8s/deployment.yaml
    ```

6. **Access the Application**:
    Once deployed, you can access the service at `http://localhost:your-port`.

## Usage

- **Login**: Users can log in with their credentials to manage rentals.
- **Fleet Monitoring**: Admins can track vehicles, monitor the status, and receive real-time updates on the dashboard.
- **Rental Transactions**: Users can browse available vehicles, create new rental orders, and manage existing rentals.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue for any feature requests or bugs.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
