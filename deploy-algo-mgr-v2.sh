#!/bin/bash

# The behaviour of this script is controlled by the following properties:
# ALGOMODULEPROPVERSION:
#    If set then all projects are updated to this version number and artifacts are deployed to remote artifactory
#    If not set then app projects are installed locally but version numbers are not changed and no remote deployment takes place
# DONOTRUNUITTESTS:
#    If set (to anything) then when projects are installed unit tests are NOT running
#    IF not set then unit tests are run as each project is installed
# BETSTARSALGOBUILD
#    If set to true then Betstars integration project is installed, and may be tested and may be deployed as above.  At present Unit tests are NOT run


error_exit()
{
      echo "*** ${PROGNAME}: ${1:-"Unknown Error"}" 1>&2
      exit 1
}

echo "*** STARTING ${PROGNAME}"
if [ -z $ALGOMODULEPROPVERSION ]
then
  echo "*** ALGOMODULEPROPVERSION is not set.  Will not update version numbers and will not deploy.  Local install (and optionally test) only" 
else 
  echo "*** ALGOMODULEPROPVERSION version is $ALGOMODULEPROPVERSION.  Will update version numbers and deploy artifacts to remote artifactory"
fi
if [ -z $DONOTRUNTESTS ]
then
  echo "*** Unit tests WILL be executed"
else
  echo "*** DONOTRUNUITTESTS is set.  Unit tests will NOT be executed"
fi

echo "*** installing ats-algo-parent..."
cd ats-algo-parent || error_exit
mvn install || error_exit
cd ../ats-algo-all || error_exit

if [ $ALGOMODULEPROPVERSION ]
then
  echo "Updating version numbers..."  
  echo "allv2"
  mvn -Pallv2 versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}]|| error_exit
  mvn -Pallv2 versions:commit || error_exit
  
  echo "ppbintegration"
  mvn -Pppbintegration versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}]|| error_exit
  mvn -Pppbintegration versions:commit || error_exit
  
  echo "customerdependencies"
  mvn -Pcustomerdependencies versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}]|| error_exit
  mvn -Pcustomerdependencies versions:commit || error_exit
  
  echo "outrightsserver"
  mvn -Poutrightsserver versions:update-property -Dproperty=outright.server.version -DnewVersion=[${ALGOMODULEPROPVERSION}]   || error_exit   
  
  # update version nos for betstars, ladbrokes even if not going to install/deploy
  echo "betstarsintegration"
  mvn -Pbetstarsintegration versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}]|| error_exit
  mvn -Pbetstarsintegration versions:commit || error_exit
  mvn -Pladbrokescoralintegration versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}]|| error_exit
  mvn -Pladbrokescoralintegration versions:commit || error_exit
  
fi

if [ -z $DONOTRUNTESTS ]
then
  echo "*** Running tests and installing..."
  echo "allv2"
  mvn -Pallv2 clean install || error_exit
  echo "ppbintegration"
  mvn -Pppbintegration clean install || error_exit
  echo "customerdependencies"
  mvn -Pcustomerdependencies clean install || error_exit
  echo "outrightsserver"
  mvn -Poutrightsserver clean install || error_exit
  if [ "$BETSTARSALGOBUILD" = "true" ]
  then
    mvn -Pbetstarsintegration -DskipTests clean install || error_exit
  fi  
else
 echo "*** Installing (without running tests)..."
  mvn -Pallv2 -DskipTests clean install || error_exit
  mvn -Pppbintegration -DskipTests clean install || error_exit
  mvn -Pcustomerdependencies -DskipTests clean install || error_exit
  mvn -Poutrightsserver -DskipTests clean install || error_exit
  if [ "$BETSTARSALGOBUILD" = "true" ]
  then
    mvn -Pbetstarsintegration -DskipTests clean install || error_exit
  fi
fi

if [ $ALGOMODULEPROPVERSION ]
then
  echo "*** Deploying to remote artifactory..."
  mvn -Pallv2 -DskipTests deploy || error_exit
  mvn -Pppbintegration -DskipTests deploy || error_exit
  mvn -Pcustomerdependencies -DskipTests deploy || error_exit
  mvn -Poutrightsserver -DskipTests deploy  || error_exit
  if [ "$BETSTARSALGOBUILD" = "true" ]
  then
    mvn -Pbetstarsintegration -DskipTests deploy || error_exit
  fi
fi


