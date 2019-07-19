서버가 다시 떠도 채팅방 보존 가능 및  여러 서버에서 접속해도 공유 가능


docker pull redis
docker run --name demo-redis -d -p 6379:6379 redis

java -jar -Dserver.port=8901 -Dspring.profiles.active=dev web.jar

java -jar -Dserver.port=8902 -Dspring.profiles.active=dev web.jar