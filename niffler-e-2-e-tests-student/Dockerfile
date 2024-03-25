ARG DOCKER
FROM $DOCKER
ENV JAVA_OPTS=""

WORKDIR /niffler
COPY ./gradle ./gradle
COPY ./gradlew ./
COPY ./niffler-e-2-e-tests-student ./niffler-e-2-e-tests-student
COPY ./niffler-grpc-common ./niffler-grpc-common
COPY ./build.gradle ./
COPY ./settings.gradle ./

CMD ls; ls niffler-e-2-e-tests-student; chmod +x ./niffler-e-2-e-tests-student/send_results.sh; ./gradlew test -Dtest.env=docker; bash ./niffler-e-2-e-tests-student/send_results.sh