##Prerequisites to build and run the application:
 - JDK 11 (or higher) with configured JAVA_HOME environment variable.

##Steps to build and run the application:
 - execute `./mvnw spring-boot:run`.

##Prerequisites to build and run the testing server:
 - Node.js v14 or higher.

##Steps to build and run the testing server:
 - change directory to 'testing_server': `cd testing_server`
 - execute `npm install`,
 - execute `node app.js`.

##Application configuration
You can configure the urls and api key used by the application in file [application.properties](config/application.properties):
 - `downstream.url.endpoint` - url to the downstream service where the events will be sent,
 - `eipa.export.dynamic.url` - url to the EIPA service that provides dynamic data for charging points,
 - `api.key` - key to authenticate to EIPA service.
