#which "official Java Image" ?
FROM openjdk:11
# working directory
WORKDIR /shop_cake_be
# coppy from your Host (PC, laptop) to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
#Run this inside the image
RUN ./mvnw dependency:go-offline
COPY src ./src
#Run inside container
CMD ["./mvnw", "spring-boot:run"]