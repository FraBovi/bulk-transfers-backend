FROM maven:3.8.3-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests=true
EXPOSE 8080
ENTRYPOINT ["java","-jar","/home/app/target/bulk-transfer-application-0.0.1-SNAPSHOT.jar"]
