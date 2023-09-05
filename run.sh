#!/bin/sh

echo "1. dashboard api server started..."

cd ~/icns-smart-plant-server/Smart-Plant-Dashboard-API

nohup java -Xmx16g -jar build/libs/Smart-Plant-Dashboard-API-0.0.1-SNAPSHOT.jar &

cd ~

exit
