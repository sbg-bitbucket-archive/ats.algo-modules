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
  export ALGOBUILDOPTION=install
else 
  echo "*** ALGOMODULEPROPVERSION version is $ALGOMODULEPROPVERSION.  Will update version numbers and deploy artifacts to remote artifactory"
  export ALGOBUILDOPTION=deploy
fi

if [ -z $DONOTRUNUNITTESTS ]
then
  echo "*** Unit tests WILL be executed"
  export ALGOSKIPTEST=false
else
  echo "*** DONOTRUNUITTESTS is set.  Unit tests will NOT be executed"
  export ALGOSKIPTEST=true
fi



if [ ! -z $ALGOMODULEPROPVERSION ]
then
	echo "*** installing ats-algo-parent..."
	cd ats-algo-parent || error_exit
	mvn versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}] || error_exit
	mvn install || error_exit

	cd ../ats-algo-all || error_exit
 	echo "Updating version numbers..."  
	echo "allv2 update version numbers" 
    mvn -Pallv2 versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}] || error_exit
    mvn -Pallv2 versions:update-child-modules -DparentVersion=[${ALGOMODULEPROPVERSION}] || error_exit
    mvn -Pallv2 versions:commit || error_exit
    
	# update version nos for betstars, ladbrokes even if not going to install/deploy
	echo "betstarsintegration update version numbers"
	#mvn -Pppbintegration versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}]|| error_exit
	#mvn -Pppbintegration versions:commit || error_exit
	mvn -Pbetstarsintegration versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}]|| error_exit
	mvn -Pbetstarsintegration versions:commit || error_exit
	mvn -Pladbrokescoralintegration versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}]|| error_exit
	mvn -Pladbrokescoralintegration versions:commit || error_exit
 	cd ..    
    
  
fi

cd ./ats-algo-all || error_exit
echo "*** Determine if Running tests and Type of build"
echo "skipTests=${ALGOSKIPTEST}"
echo "Build option=${ALGOBUILDOPTION}"
echo "allv2 build profile"
mvn -Pallv2 -DskipTests=$ALGOSKIPTEST clean ${ALGOBUILDOPTION} || error_exit
#echo "ppbintegration build profile"
#mvn -Pppbintegration -DskipTests=$ALGOSKIPTEST clean ${ALGOBUILDOPTION} || error_exit
#echo "customerdependencies build profile"
#mvn -Pcustomerdependencies -DskipTests=$ALGOSKIPTEST clean ${ALGOBUILDOPTION} || error_exit
#echo "outrightsserver build profile"
#mvn -Poutrightsserver -DskipTests=$ALGOSKIPTEST clean ${ALGOBUILDOPTION} || error_exit

if [ "$BETSTARSALGOBUILD" = "true" ]
  then
  mvn -Pbetstarsintegration -DskipTests=$ALGOSKIPTEST clean ${ALGOBUILDOPTION} || error_exit
fi  

if [ "$PPBALGOBUILD" = "true" ]
  then
  mvn -Pppbintegration -DskipTests=$ALGOSKIPTEST clean ${ALGOBUILDOPTION} || error_exit
fi
