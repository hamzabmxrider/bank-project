FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
COPY bank-core/pom.xml bank-core/pom.xml
RUN mvn -B -ntp -pl bank-core -am dependency:go-offline

COPY . .
RUN mvn -B -ntp -pl bank-core -am -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

COPY --from=build /workspace/bank-core/target/ /tmp/target/
RUN set -eux; \
    JAR_FILE="$(find /tmp/target -maxdepth 1 -name '*.jar' ! -name '*original*' | head -n 1)"; \
    cp "$JAR_FILE" /app/app.jar; \
    rm -rf /tmp/target

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
