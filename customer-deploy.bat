cd ats-algo-all
call mvn -Pall -DskipTests clean install

echo changing to ats-algo-parent
cd ..
cd ats-algo-parent

echo update ats-algo-parent and ps-algo-tennis version numbers.
pause

rem Check current version and update the minor number (typically), e.g. 0.12.0 -> 0.13.0
rem Manually update the version on both lines that end with <!-- ALGO VERSION -->
rem If DTO has changed, updated version in property 'algo.dto.version'
echo ats-algo-parent install
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
call mvn -DskipTests -Pall clean deploy

echo changing to ats-algo-customer-dependencies
cd ..
cd ats-algo-customer-dependencies

call mvn versions:update-parent
call mvn versions:commit
call mvn -Palgo-repo -DskipTests deploy
call mvn -DskipTests deploy

echo All steps are done.
echo now commit and push changes using
echo git pull
echo git commit -m "updated versions" -a
echo git push
pause
