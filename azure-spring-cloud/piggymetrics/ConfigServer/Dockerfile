FROM openjdk:12.0.2

EXPOSE 8888

ADD ./target/ConfigServer-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]