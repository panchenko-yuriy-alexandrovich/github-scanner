FROM ubuntu:bionic as server-build
RUN apt-get update && apt-get install -y openjdk-11-jre-headless && apt-get clean

WORKDIR /app

COPY mvnw ./
COPY .mvn .mvn/
RUN ./mvnw --version

COPY pom.xml ./
RUN ./mvnw clean package --fail-never

COPY . ./
RUN ./mvnw clean package -DskipTests

FROM bellsoft/liberica-openjre-alpine:11 as final
RUN adduser -S user

WORKDIR /app

COPY --from=server-build /app/target/libs libs/
COPY --from=server-build /app/target/app.jar ./

RUN mkdir tmp; chown -R user tmp

USER user

ENV JAVA_OPTS="-Xmx330m -Xss512k"
ENV PORT=8080
ENV DATABASE_URL=""

EXPOSE $PORT

CMD java $JAVA_OPTS -jar app.jar