FROM maven:3.8.3-openjdk-17 AS build
COPY bulk-transfer-application/src /home/app/src
COPY bulk-transfer-application/pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests=true
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/app/target/bulk-transfer-application-0.0.1-SNAPSHOT.jar"]
