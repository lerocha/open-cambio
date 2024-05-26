FROM amazoncorretto:21-alpine

RUN mkdir /opt/open-cambio
COPY build/libs/open-cambio-0.0.2-SNAPSHOT.jar /opt/open-cambio/open-cambio-0.0.2-SNAPSHOT.jar

ENTRYPOINT java -jar /opt/open-cambio/open-cambio-0.0.2-SNAPSHOT.jar
