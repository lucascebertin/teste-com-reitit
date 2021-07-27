FROM clojure:openjdk-11-lein-slim-buster as builder



WORKDIR /usr/src/app

# install main deps, sometimes change
COPY project.clj .
RUN lein deps

COPY resources/ /usr/src/app/resources
COPY src/ /usr/src/app/src
COPY test/ /usr/src/app/test

RUN lein uberjar

# use clean base image
FROM openjdk:13-slim-buster

ENV PORT 3000
EXPOSE 3000

CMD ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=85","-XX:+UnlockExperimentalVMOptions","-XX:+UseZGC","-jar","/usr/src/app/app.jar"]
COPY --from=builder /usr/src/app/target/uberjar/teste-com-reitit-0.1.0-SNAPSHOT-standalone.jar /usr/src/app/app.jar

