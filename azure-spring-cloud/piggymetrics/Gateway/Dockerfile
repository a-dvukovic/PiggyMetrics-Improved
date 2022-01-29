FROM openjdk:12.0.2

EXPOSE 4000

ADD ./target/gateway-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]