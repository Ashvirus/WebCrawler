# WebCrawler

You can clone this project into your local environment setup.

## Steps to setup your local and run the program from terminal/cmd:
1. git clone https://github.com/Ashvirus/WebCrawler.git
2. cd crawler
3. mvn clean install
4. mvn spring-boot:run -Dspring-boot.run.arguments=<your base url> 
   Example:  mvn spring-boot:run -Dspring-boot.run.arguments=https://www.joinlane.com/
5. You can see the logs in your terminal.
6. Once the program ran successfully then you can see an excel file name crawler.xlsx in your folder structure with crawled details.
