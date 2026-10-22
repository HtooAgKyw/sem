FROM eclipse-temurin:25

WORKDIR /app

COPY ./target/seMethods.jar /app/seMethods.jar

ENTRYPOINT ["java", "-jar", "seMethods.jar", "db:3306", "10000"]