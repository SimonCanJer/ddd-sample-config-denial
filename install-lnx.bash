mvn     install
cd target
java -jar denial-service-spring-1.0-SNAPSHOT-exec.jar server &
sleep 20
java -jar denial-service-spring-1.0-SNAPSHOT-exec.jar client &
