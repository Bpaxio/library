FROM maven:3.6.1-jdk-8
ENV PROJECT_DIR=/opt/project

RUN mkdir -p $PROJECT_DIR

ADD pom.xml $PROJECT_DIR
ADD src/ $PROJECT_DIR/src
WORKDIR $PROJECT_DIR

RUN mvn dependency:resolve
RUN mvn install


FROM openjdk:8-jdk
ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=0 $PROJECT_DIR/target/library* $PROJECT_DIR/
EXPOSE 8080

CMD ["java", "-jar", "-Dspring.profiles.active=docker", "/opt/project/library-0.2.0-SNAPSHOT.jar"]