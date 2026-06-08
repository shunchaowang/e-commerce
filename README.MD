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