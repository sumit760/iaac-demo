FROM tomcat
MAINTAINER pwc

 
COPY Presentation-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/Presentation-0.0.1-SNAPSHOT.war

CMD ["catalina.sh", "run"]
