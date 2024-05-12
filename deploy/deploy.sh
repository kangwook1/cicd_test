#!/bin/bash
echo "> 현재 USER NAME을 가져옵니다."
CURRENT_USER=$(whoami)
echo "> download 폴더에서 build된 .jar 파일을 가져옵니다."
cp /home/$CURRENT_USER/download/build/libs/*.jar /home/$CURRENT_USER/deploy;
echo "> deploy 폴더에서 plain.jar 파일을 찾고, 파일이 있다면 삭제합니다."
find /home/$CURRENT_USER/deploy -name "*plain*" | xargs rm;
echo "> download 폴더를 삭제합니다."
rm -rf /home/$CURRENT_USER/download;

 echo "> 작동 중인 [JAR 이름]을 찾습니다."
 CURRENT_PID=$(pgrep -fo [JAR 이름])
 echo "$CURRENT_PID"
 if [ -z $CURRENT_PID ]; then
         echo "> 실행 중인 [JAR 이름]이 없습니다."
 else
         echo "> kill -9 $CURRENT_PID"
         kill -9 $CURRENT_PID
         sleep 3
 fi
 echo "> [JAR 이름]을 시작합니다."


nohup java -jar *.jar 1>stdout.txt 2>stderr.txt &

sleep 3