FROM openjdk:8 AS build

RUN apt-get update -q \
  && apt-get install -qy --no-install-recommends \
  maven

COPY . /build
WORKDIR /usr/src/app
RUN cd /build \
    && mvn -q clean \
    && mvn -q -DskipTests package \
    && cp /build/target/*.jar /usr/src/app/app.jar \
    && mvn -q clean

FROM ubuntu:focal

ENV JDK_URL=https://corretto.aws/downloads/latest/
ENV JDK_TGZ=amazon-corretto-8-x64-linux-jdk.tar.gz
ENV JDK_VERSION=8

ENV TZ=Etc/UTC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update -q \
  && apt-get install -qy --no-install-recommends \
  bzip2 \
  ca-certificates \
  curl \
  fontconfig \
  libfreetype6 \
  locales \
  p11-kit \
  unzip \
  xz-utils \
  gnupg \
  && rm -rf /var/lib/apt/lists/* \
  && locale-gen en_US.UTF-8

ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

RUN mkdir -p /usr/lib/jvm/java
WORKDIR /usr/lib/jvm/

RUN curl -L -k -s ${JDK_URL}${JDK_TGZ} -o ./${JDK_TGZ} \
	&& tar zxf ./${JDK_TGZ} -C java --strip-components=1 \
	&& rm -rf ./${JDK_TGZ} \
	&& ln -Ffs /usr/lib/jvm/java/bin/java /usr/bin/java \
	&& ln -Ffs /usr/lib/jvm/java/bin/* /usr/local/bin/

ENV GOOGLE_APPLICATION_CREDENTIALS="/auth.json"

WORKDIR /usr/src/app
COPY --from=build /usr/src/app/app.jar /usr/src/app/app.jar

ENV PORT 8080
EXPOSE $PORT

ENTRYPOINT [ "sh", "-c", "java -jar ./app.jar" ]