FROM mozilla/sbt

ADD build.sbt /root/build/
RUN cd /root/build && sbt compile

EXPOSE 5000
WORKDIR /root/build

CMD sbt run
