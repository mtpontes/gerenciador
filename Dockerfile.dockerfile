FROM tomcat:10.1.13-jre17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/gerenciador.war /usr/local/tomcat/webapps/gerenciador.war

COPY docker-run-gerenciador.sh /usr/local/tomcat/docker-run-gerenciador.sh
RUN chmod +x /usr/local/tomcat/start.sh

EXPOSE 8080

CMD ["catalina.sh", "run"]
