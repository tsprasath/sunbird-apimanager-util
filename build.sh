#!/bin/sh
# Build script
set -e
e () {
    echo $( echo ${1} | jq ".${2}" | sed 's/\"//g')
}
m=$(./src/metadata.sh)

org=$(e "${m}" "org")
name=$(e "${m}" "name")
version=$(e "${m}" "version")

./gradlew build --stacktrace
docker build -f ./Dockerfile -t ${org}/${name}:${version}-bronze .