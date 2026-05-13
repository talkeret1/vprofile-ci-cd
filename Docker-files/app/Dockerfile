### Run Docker-Compose Locally (without Jenkins) ###

# FROM maven:3.9.9-eclipse-temurin-21-jammy AS build_image
# RUN git clone -b containers https://github.com/hkhcoder/vprofile-project.git
# RUN cd vprofile-project && mvn install

# FROM tomcat:10-jdk21
# LABEL "Name"="Vprofile Project JAVA Web App"
# RUN rm -rf /usr/local/tomcat/webapps/*
# COPY --from=build_image vprofile-project/target/vprofile-v2.war /usr/local/tomcat/webapps/ROOT.war
# EXPOSE 8080
# CMD ["catalina.sh","run"]


FROM maven:3.9.9-eclipse-temurin-21-jammy AS build_image

WORKDIR /vprofile

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10-jdk21

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build_image /vprofile/target/vprofile-v2.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]