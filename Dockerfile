FROM maven:3-amazoncorretto-20 as build
COPY ./ /usr/src/server
WORKDIR /usr/src/server
RUN mvn -q clean \
    && mvn -q -DskipTests package \
    && cp target/spaceboot-*.jar /app.jar

FROM amazoncorretto:20

WORKDIR /usr/src/app
COPY --from=build /app.jar /app.jar

ENV PORT 8080
EXPOSE $PORT

ENTRYPOINT [ "sh", "-c", "java -jar ./app.jar" ]