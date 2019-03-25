cd C:/Users/ankus/Documents/NetBeansProjects/CECS521Project/src/cecs521project

set path=C:/Program Files/Java/jdk1.8.0_141/bin

javac -classpath .:/Users/ankus/Documents/NetBeansProjects/CECS521Project;mysql-connector-java-5.1.47-bin.jar Populate.java Zone.java Officer.java Route.java Incident.java DatabaseConnection.java

java -classpath .:/Users/ankus/Documents/NetBeansProjects/CECS521Project;mysql-connector-java-5.1.47-bin.jar Populate db.properties zone.txt officer.txt route.txt incident.txt

pause >nul