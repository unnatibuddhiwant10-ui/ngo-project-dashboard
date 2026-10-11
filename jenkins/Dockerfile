FROM jenkins/jenkins:lts-jdk21

USER root

RUN apt-get update && \
    apt-get install -y docker.io curl maven chromium chromium-driver && \
    rm -rf /var/lib/apt/lists/*

RUN jenkins-plugin-cli --plugins workflow-aggregator git credentials-binding

ENV CHROME_BIN=/usr/bin/chromium

USER jenkins