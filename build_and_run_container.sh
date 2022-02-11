#!/bin/bash

./gradlew clean build

version=$(git rev-parse --short HEAD)

docker build --tag=hotel-reservation-server:"$version" .

docker tag hotel-reservation-server:"$version" aaronburk/hotel-reservation-server:"$version"

docker push aaronburk/hotel-reservation-server:"$version"

docker run -p8884:8084 hotel-reservation-server:"$version"