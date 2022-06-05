FROM bellsoft/liberica-openjdk-alpine-musl:17
COPY target/*.jar logger.jar
CMD java -Xms256M -Xmx256M -XX:ActiveProcessorCount=2 -jar /logger.jar
