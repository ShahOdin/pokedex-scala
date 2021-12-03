FROM mozilla/sbt

ADD build.sbt /root/build/
RUN cd /root/build && sbt compile
WORKDIR /root/build
