#!/bin/sh

if [ -z "${SKY_HOME}" ]; then
  SKY_HOME=$(dirname $BASH_SOURCE)/..
fi

cd ${SKY_HOME}
nohup java -Dspring.config.location=file:conf/sky-worker.properties -jar bin/sky-worker.jar 1> /dev/null 2>&1 &