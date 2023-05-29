FROM openjdk:18.0.2-jdk-oracle
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} 30642_STEAU_IULIU_COURT_RESERVE_BACKEND-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","30642_STEAU_IULIU_COURT_RESERVE_BACKEND-0.0.1-SNAPSHOT.jar"]