FROM jenkins/jenkins:lts
COPY jenkins.yaml /var/jenkins_home/casc_configs/jenkins.yaml
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY init.groovy.d /var/jenkins_home/init.groovy.d/
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt
ENV CASC_JENKINS_CONFIG=/var/jenkins_home/casc_configs