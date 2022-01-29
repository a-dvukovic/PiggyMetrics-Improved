# 
# Changes done to the original Piggymetrics app
#

- removed docker-compose-dev
- added openfeign config to statistics-service test
- removed turbine
- removed hystrix-stream
- added new Eureka,Gateway and Config Services
- upgraded all microservices except for Authorization to Spring Boot 2.6.0
- Upgrades the microservice tests for each service from jUnit 4 to jUnit 5