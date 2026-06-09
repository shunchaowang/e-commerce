# Project: E-commerce

This is a e-commerce application built with a microservices architecture. The system allows the
customers to browse products, place orders and receive notifications. The whole functionality
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
                                                
## Core Services
### Product Service
### Order Service
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

- Grafana: visualization dashboard for all observability data
- Prometheus: time-series database for collecting and querying metrics
- Loki: log aggregation system (similar to Elasticsearch)
- Tempo: distributed tracing backend

