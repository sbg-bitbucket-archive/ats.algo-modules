cd ats-algo-all
call mvn -DskipTests clean install

cd ..
cd ats-algo-algomanager
call mvn -DskipTests clean install

cd..
cd ats-algo-parent
call mvn install

cd ..
cd ats-algo-all
call mvn versions:update-parent
call mvn versions:commit
call mvn -Pcore versions:update-parent
call mvn -Pcore versions:commit
call mvn -Pcore -DskipTests clean install
call mvn -Psport-parent versions:update-parent
call mvn -Psport-parent versions:commit
call mvn -Psport-parent install
call mvn -Pall versions:update-parent
call mvn -Pall versions:commit
call mvn -DskipTests -Pall clean install

cd ..
cd ats-algo-sport-parent
call mvn -DskipTests clean install

cd ..
cd ats-algo-parent
call mvn deploy

cd ..
cd ats-algo-all
call mvn -DskipTests -Pcore deploy

cd ..
cd ats-algo-customer-dependencies
call mvn versions:update-parent
call mvn versions:commit
call mvn -Palgo-repo -DskipTests deploy

cd ..
call cd ats-algo-sport-parent
call mvn deploy

cd ..
cd ats-algo-remote-pfpc
call mvn -DskipTests deploy

cd ..
cd ats-algo-algomanager-bootstrap
call mvn -DskipTests deploy

cd ..
cd example-amelco-algo-tennis
call mvn -o versions:use-latest-releases
call mvn versions:commit

cd ..
cd betstars-integration
call mvn versions:update-parent
call mvn versions:commit
call mvn -DskipTests clean deploy

cd ..
cd ladbrokescoral-integration
call mvn versions:update-parent
call mvn versions:commit
call mvn -DskipTests clean deploy

echo complete
pause



