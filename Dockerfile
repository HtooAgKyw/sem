FROM eclipse-temurin:25
LABEL authors="USER"
COPY ./target/classes/com /tmp/com
WORKDIR /tmp
ENTRYPOINT ["java", "com.napier.sem.App"]