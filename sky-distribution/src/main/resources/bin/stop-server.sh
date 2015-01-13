#!/bin/bash

if [ -z "${SKY_HOME}" ]; then
  SKY_HOME=$(dirname $BASH_SOURCE)/..
fi

cd ${SKY_HOME}
if [ -d "sky-server.pid" ]; then
  kill $(cat sky-server.pid)
else
  echo "sky-server is not started"
fi