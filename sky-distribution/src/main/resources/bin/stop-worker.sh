#!/bin/sh

if [ -z "${SKY_HOME}" ]; then
  SKY_HOME=$(dirname $BASH_SOURCE)/..
fi

cd ${SKY_HOME}
if [ -d "sky-worker.pid" ]; then
  kill $(cat sky-worker.pid)
else
  echo "sky-worker is not started"
fi