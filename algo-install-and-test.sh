#!/bin/bash

error_exit()
{
      echo "${PROGNAME}: ${1:-"Unknown Error"}" 1>&2
      exit 1
}

echo "install and test all algo projects"
cd ats-algo-parent
mvn install || error_exit

cd ../ats-algo-all || error_exit
mvn -Pall install || error_exit

cd .. || error_exit


