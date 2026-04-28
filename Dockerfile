ARG MODULE_PATH=bank-core

FROM maven:3.9.9-eclipse-temurin-17 AS build
ARG MODULE_PATH
WORKDIR /workspace

COPY pom.xml .
COPY ${MODULE_PATH}/pom.xml ${MODULE_PATH}/pom.xml
RUN mvn -B -ntp -pl "${MODULE_PATH}" -am dependency:go-offline

COPY . .
RUN mvn -B -ntp -pl "${MODULE_PATH}" -am -DskipTests package

FROM eclipse-temurin:17-jre-jammy
ARG MODULE_PATH
WORKDIR /app

COPY --from=build /workspace/${MODULE_PATH}/target/ /tmp/target/
RUN set -eux; \
    JAR_FILE="$(find /tmp/target -maxdepth 1 -name '*.jar' ! -name '*original*' | head -n 1)"; \
    cp "$JAR_FILE" /app/app.jar; \
    rm -rf /tmp/target

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
