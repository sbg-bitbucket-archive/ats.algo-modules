#!/bin/bash

error_exit()
{
      echo "${PROGNAME}: ${1:-"Unknown Error"}" 1>&2
      exit 1
}

if [ -z $ALGOMODULEPROPVERSION ]
then
  echo "NO ALGOMODULEPROPVERSION is set" 
  exit 1
fi

echo "${PROGNAME}"
echo "ALGOMODULEPROPVERSION version is $ALGOMODULEPROPVERSION"

cd ats-algo-parent || error_exit
mvn install || error_exit

cd ../ats-algo-all || error_exit
mvn versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}]|| error_exit
mvn versions:commit || error_exit
mvn -Pcore versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}] || error_exit
mvn -Pcore versions:commit || error_exit
mvn -Pcore -DskipTests clean install || error_exit
mvn -Psport-parent versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}] || error_exit
mvn -Psport-parent versions:commit || error_exit
mvn -Psport-parent install || error_exit
mvn -Pall versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}] || error_exit
mvn -Pall versions:commit || error_exit
mvn -DskipTests -Pall clean install || error_exit

#cd .. || error_exit
#cd example-amelco-algo-tennis || error_exit
#mvn -o versions:use-latest-releases
#mvn versions:commit || error_exit

echo "ats-algo-parent deploy"
cd ../ats-algo-parent || error_exit
if [ "$AMELCOALGOBUILD" = "true" ]
then
mvn deploy || error_exit
else
mvn install || error_exit
fi

echo "ats-algo-all deploy"
cd ../ats-algo-all || error_exit
if [ "$AMELCOALGOBUILD" = "true" ]
then
mvn -DskipTests -Pcore deploy || error_exit
else
mvn -DskipTests -Pcore install || error_exit
fi

echo "ats-algo-customer-dependencies deploy"
cd ../ats-algo-customer-dependencies || error_exit
mvn versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}] || error_exit
mvn versions:commit || error_exit
if [ "$AMELCOALGOBUILD" = "true" ]
then
mvn -Palgo-repo -DskipTests deploy || error_exit
else
mvn -Palgo-repo -DskipTests install || error_exit
fi

echo "ats-algo-sport-parent deploy"
cd ../ats-algo-sport-parent || error_exit
if [ "$AMELCOALGOBUILD" = "true" ]
then
mvn deploy || error_exit
else
mvn install || error_exit
fi

echo "ats-algo-remote-pfpc deploy"
cd ../ats-algo-remote-pfpc || error_exit
if [ "$AMELCOALGOBUILD" = "true" ]
then
mvn -DskipTests deploy || error_exit
else
mvn -DskipTests install || error_exit
fi

echo "ats-algo-algomanager-bootstrap deploy"
cd ../ats-algo-algomanager-bootstrap || error_exit
if [ "$AMELCOALGOBUILD" = "true" ]
then
mvn -DskipTests deploy || error_exit
else
mvn -DskipTests install || error_exit
fi

echo "ats-algo-sport-outrights-server.jar deploy"
cd ../ats-algo-sport-outrights-server || error_exit
mvn versions:update-property -Dproperty=outright.server.version -DnewVersion=[${ALGOMODULEPROPVERSION}]   || error_exit

cd ../ats-algo-sport-outrights || error_exit
mvn -DskipTests clean install || error_exit

cd ../ats-algo-sport-outrights-api || error_exit
mvn -DskipTests clean install || error_exit

cd ../ats-algo-sport-outrights-calcengine || error_exit
mvn -DskipTests clean install || error_exit

cd ../ats-algo-sport-outrights-server || error_exit
mvn -DskipTests clean install || error_exit

mvn versions:commit || error_exit
if [ "$AMELCOALGOBUILD" = "true" ]
then
mvn -DskipTests deploy || error_exit
else
mvn -DskipTests install || error_exit
fi


if [ "$BETSTARSALGOBUILD" = "true" ]
then
	echo "betstars-integration deploy"
	cd ../betstars-integration || error_exit
	mvn versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}] || error_exit
	mvn versions:commit || error_exit
	if [ "$AMELCOALGOBUILD" = "true" ]
	then
	mvn -DskipTests clean deploy || error_exit
	else
	mvn -DskipTests clean install || error_exit
	fi
fi

#cd ../ladbrokescoral-integration || error_exit
#mvn versions:update-parent -DparentVersion=[${ALGOMODULEPROPVERSION}] || error_exit
#mvn versions:commit || error_exit
#if [ "$AMELCOALGOBUILD" = "true" ]
#then
#mvn -DskipTests clean deploy || error_exit
#else 
#mvn -DskipTests clean install || error_exit
#fi

cd .. || error_exit


