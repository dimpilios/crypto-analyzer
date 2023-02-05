Application loads data from csv files at startup. File paths are provided using the following JVM params:

-Ddata.file.path.property.format=data.file.path
-Ddata.file.path.btc="C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\BTC_values_test.csv"
-Ddata.file.path.doge="C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\DOGE_values_test.csv"
-Ddata.file.path.eth="C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\ETH_values_test.csv"
-Ddata.file.path.ltc="C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\LTC_values_test.csv"
-Ddata.file.path.xrp="C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\XRP_values_test.csv"
-DLOG_PATH=${user.home}/crypto-analyzer-logs/
-DLOG_FILE=crypto-analyzer.log

OpenAPI documentation is live served by the app in the url http://localhost:8080/swagger-ui/index.html
Also, OpenAPI documentation is generated at build time by springdoc-openapi-maven-plugin and can be found
under /target/openapi.json


To build application using Maven you can run the following command. argLine parameter is needed for E2E tests in CryptoAnalyzerE2ETest class which loads the entire Spring context 
spring-boot-maven-plugin uses its own parameters in the plugin configuration unit inside pom. These are needed when the plugin is used by springdoc-openapi-maven-plugin which generates
OpenApi REST documentation by loading context.

mvn -DargLine="-Ddata.file.path.property.format=data.file.path -Ddata.file.path.btc=C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\BTC_values_test.csv -Ddata.file.path.doge=C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\DOGE_values_test.csv -Ddata.file.path.eth=C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\ETH_values_test.csv -Ddata.file.path.ltc=C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\LTC_values_test.csv -Ddata.file.path.xrp=C:\\Users\\dimpi\\OneDrive\\Desktop\\Crypto_exercise\\prices\\XRP_values_test.csv -DLOG_PATH=${user.home}/crypto-analyzer-logs/ -DLOG_FILE=crypto-analyzer.log" clean install


Maven build generates Javadoc under /target/apidocs

Maven build generates Test Coverage analysis under /target/apidocs


Some tests use some crypto .csv files. These can be found under /prices directory on the same level with source code directory.

