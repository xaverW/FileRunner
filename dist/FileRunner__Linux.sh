#!/bin/sh
#

dir=$(dirname $(readlink -f "$0"))
cd "$dir"

if [ -n "$JAVA_HOME" ]; then
  $JAVA_HOME/bin/java -jar ./FileRunner.jar "$@"
else
  java -jar ./FileRunner.jar "$@"
fi

cd $OLDPWD
