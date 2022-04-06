@echo off
wsl -d docker-desktop bash -ic "sysctl -w vm.max_map_count=262144"
docker network create elastic
docker-compose up -d

pause