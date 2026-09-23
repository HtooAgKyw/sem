FROM eclipse-temurin:25

LABEL authors="USER"

COPY ./target/seMethods-2.0.0-jar-with-dependencies.jar /tmp/

WORKDIR /tmp

ENTRYPOINT ["java", "-jar", "seMethods-2.0.0-jar-with-dependencies.jar"]