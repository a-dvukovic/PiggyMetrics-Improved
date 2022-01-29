#!/usr/bin/env bash

# ====== Piggy Metrics Azure Coordinates
export RESOURCE_GROUP=ELXSpeciality
export REGION=eastus
export SPRING_CLOUD_SERVICE=petlic-spring-cloud

# ====== Piggy Metrics Environment Variables
export CONFIG_SERVER_URI=http://config:8888

# ====== Some fake passwords
export ACCOUNT_SERVICE_PASSWORD=XUoJBrTtqXBonU5zMVzSUtrLPKRQztLUQE4poDoIR1QdcDfGgnGgJO5wbFC7xCEL
export NOTIFICATION_SERVICE_PASSWORD=XUoJBrTtqXBonU5zMVzSUtrLPKRQztLUQE4poDoIR1QdcDfGgnGgJO5wbFC7xCEL
export STATISTICS_SERVICE_PASSWORD=XUoJBrTtqXBonU5zMVzSUtrLPKRQztLUQE4poDoIR1QdcDfGgnGgJO5wbFC7xCEL

export SMTP_USER=dev-user
export SMTP_PASSWORD=dev-password

# ====== Piggy Metrics Project JAR coordinates
export ACCOUNT_SERVICE_JAR=account-service/target/account-service.jar
export AUTH_SERVICE_JAR=auth-service/target/auth-service.jar
export GATEWAY_JAR=gateway/target/gateway.jar
export NOTIFICATION_SERVICE_JAR=notification-service/target/notification-service.jar
export STATISTICS_SERVICE_JAR=statistics-service/target/statistics-service.jar
export TURBINE_STREAM_SERVICE_JAR=turbine-stream-service/target/turbine-stream-service.jar
export MONITORING_JAR=monitoring/target/monitoring.jar

## ===== Mongo DB
export MONGODB_DATABASE=mongodb-accnt
export MONGODB_USER=mongodb-accnt
export MONGODB_URI="mongodb://mongodb-accnt:UMr7tMwqwECX3meQiLuiPc4CvFIP2Pdr1S8m8FTveuTVXY1S7nfeD7Y7wwBkiqvKIAobu0kuDcUQchknkqDBqA==@mongodb-accnt.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb&retrywrites=false&maxIdleTimeMS=120000&appName=@mongodb-accnt@"
export MONGODB_RESOURCE_ID=mongodb-accnt

## ===== Rabbit MQ
export RABBITMQ_RESOURCE_GROUP=ELXSpeciality
export VM_NAME=RabbitMQ-VM-Lin
export ADMIN_USERNAME=azureuser

# Rabbit MQ
export RABBITMQ_HOST=23.99.229.53
export RABBITMQ_PORT=5672
export RABBITMQ_USERNAME=azureuser
export RABBITMQ_PASSWORD=L0veyour$elf