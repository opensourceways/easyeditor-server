FROM ubuntu:20.04

MAINTAINER zhongjun <jun.zhongjun2@gmail.com>

RUN mkdir -p /var/lib/easyedit
WORKDIR /var/lib/easyedit

RUN apt-get update

RUN apt install --yes openjdk-17-jdk
RUN apt-get install --yes wget
RUN apt-get install --yes git

RUN wget https://dlcdn.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz && \
        tar -xzvf apache-maven-3.6.3-bin.tar.gz
ENV MAVEN_HOEM=/var/lib/easyedit/apache-maven-3.6.3
ENV PATH=$MAVEN_HOEM/bin:$PATH

RUN git clone https://github.com/opensourceways/easyedit && \
        cd easyedit && \
        mvn clean install package -Dmaven.test.skip && \
        mv ./target/easyedit-0.0.1-SNAPSHOT.jar ../easyedit.jar

CMD java -jar easyedit.jar
