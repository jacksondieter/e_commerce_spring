FROM jenkinsci/blueocean
USER root
RUN apk add --update --no-cache openssh maven && \
    ssh-keygen -t rsa -N "" -f /root/.ssh/id_rsa && \
    chmod 600 /root/.ssh/id_rsa