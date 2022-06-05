FROM bellsoft/liberica-openjdk-alpine-musl:17
COPY target/*.jar notification.jar
CMD java -Xms512M -Xmx512M -XX:ActiveProcessorCount=2 -jar /notification.jar
