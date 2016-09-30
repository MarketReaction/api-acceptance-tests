FROM hypriot/rpi-java:1.8.0
ADD lib/ lib/
ADD api-acceptance-tests-test-jar-with-dependencies.jar api-acceptance-tests-test-jar-with-dependencies.jar
CMD java -cp lib/junit-4.12.jar:api-acceptance-tests-test-jar-with-dependencies.jar org.junit.runner.JUnitCore com.jassoft.markets.ApiAcceptanceTest.ApiTestSuite