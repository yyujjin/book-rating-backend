FROM openjdk:17-alpine
COPY ./build/libs/bookrating-0.0.1-SNAPSHOT.jar /build/libs/bookrating-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","/build/libs/bookrating-0.0.1-SNAPSHOT.jar"]