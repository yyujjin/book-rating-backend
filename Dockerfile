FROM openjdk:17-alpine
WORKDIR /build/libs
COPY ./build/libs/bookrating-0.0.1-SNAPSHOT.jar /build/libs/bookrating-0.0.1-SNAPSHOT.jar
COPY .env /build/libs/
CMD ["java","-jar","/build/libs/bookrating-0.0.1-SNAPSHOT.jar"]