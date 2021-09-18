FROM adoptopenjdk/openjdk14

EXPOSE 8105

ADD target/jswitch-0.0.1-SNAPSHOT.jar /opt/jswitch-0.0.1-SNAPSHOT.jar

WORKDIR /opt

COPY . /opt



ENTRYPOINT ["java", "-jar" , "jswitch-0.0.1-SNAPSHOT.jar", "&"]