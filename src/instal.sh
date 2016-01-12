mkdir /opt/sekai
chown tomcat7:tomcat7 /opt/sekai
cp /var/lib/tomcat7/webapps/ROOT/WEB-INF/lib/mysql-connector-java-5.1.38-bin.jar /usr/share/tomcat7/lib/
sudo service tomcat7 restart
