# Introduction
Product service is a microservice that provides product management functionality. We use it to manage products in our e-commerce platform.

# Setup
- start mongodb by running `docker compose up -d`
- start product service

# Access Mongodb 
There are multi ways to access mongodb. 
- Use `docker exec` to run commands inside the mongodb container
1. run `docker ps` to get the container id
2. run `docker exec -it <container_id> mongosh -u root -p password` to connect to mongodb
3. or run `docker exec -it <container_id> bash` get into the container and run `mongosh -u root -p password`
- Connect to mongodb in Intellij Idea `mongodb://root:password@localhost:27017`

# Stop the services
- stop mongodb by running `docker compose down`