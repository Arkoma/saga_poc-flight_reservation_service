#!/bin/bash

./gradlew clean build

version=$(git rev-parse --short HEAD)

docker build --tag=flight-reservation-server:"$version" .

docker tag flight-reservation-server:"$version" aaronburk/flight-reservation-server:"$version"

docker push aaronburk/flight-reservation-server:"$version"

docker run -p8884:8084 flight-reservation-server:"$version"