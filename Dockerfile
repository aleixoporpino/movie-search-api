FROM openjdk:17-jdk
MAINTAINER porpapps
COPY target/movie-search-api-1.0.0.jar movie-search-api-1.0.0.jar
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "/movie-search-api-1.0.0.jar"]