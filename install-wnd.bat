call mvn install
cd target
echo "starting server "
echo "!!!! PLEASE FIND INFORMATION WHERE IT IS INSTALLED and ENTER THIS PORT WHEN CLIENT IS BEING INITLAIZED"
start java -jar denial-service-spring-1.0-SNAPSHOT-exec.jar server
echo "!!!! YET ONCE, FIND THE PORT NUMBER WHERE SERVER LISTENS"
echo "YOU HAVE 20 SECOND TO SEE"
timeout 20
start java -jar denial-service-spring-1.0-SNAPSHOT-exec.jar client


