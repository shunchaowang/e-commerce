# Project: E-commerce

This is a e-commerce inventory management application built with a microservices architecture. The system allows the
business owners to browse products, manage inventory and receive notifications. The whole functionality
is split into specialized services that communicate with each other through kafka as the message
broker.

The project implements several crucial microservice architectural patterns:

- Service Discovery
- API Gateway
- Circuit Breaker
- Event driven architecture
- Observability

## Project Architecture

                                                +---------------------------+
                                                |                           |
                                                |                           |
                                                +---------------------------+
                                                |                           |
                                                |                           |
                                                +---------------------------+


## Build

Under the root path, run `./gradlew build` to build the whole project.

## Test

Before test make sure the podman has started by running `podman machine start`, then run `./gradlew test`.

## Core Services

### Product Service

### Inventory Service

### Notification Service

### API Gateway

I implemented an API Gateway that serves as the entry point for all client requests. This simplified client interactions
and provided a centralized point for cross-cutting concerns like security and routing.

## Security with Keycloak

## Circuit Breaker Pattern (Resilience4J)

## Asynchronous Communication (Kafka)

## Observability with Grafana Stack

I integrated the complete Grafana stack to implement comprehensive observability across all services:

- **Grafana**: visualization dashboard for all observability data
- **Prometheus**: time-series database for collecting and querying metrics
- **Loki**: log aggregation system (similar to Elasticsearch)
  When running the application without passing -Dspring.profiles.active=loki|prod, the LOKI appender will be bypassed entirely.
- **Tempo**: distributed tracing backend
