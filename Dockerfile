FROM eclipse-temurin:21-jre AS builder
WORKDIR /application

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar

# extract into a clean directory
RUN mkdir extracted \
 && java -Djarmode=tools -jar application.jar extract --layers --launcher --destination extracted


FROM eclipse-temurin:21-jre
WORKDIR /application

COPY --from=builder /application/extracted/dependencies/ ./
COPY --from=builder /application/extracted/snapshot-dependencies/ ./
COPY --from=builder /application/extracted/spring-boot-loader/ ./
COPY --from=builder /application/extracted/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]

EXPOSE 8080
