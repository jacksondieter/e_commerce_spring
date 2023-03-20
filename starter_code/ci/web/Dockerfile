FROM tomcat:jdk8
RUN cp -r webapps.dist/* webapps
COPY tomcat-users.xml conf/
COPY context.xml webapps/manager/META-INF/