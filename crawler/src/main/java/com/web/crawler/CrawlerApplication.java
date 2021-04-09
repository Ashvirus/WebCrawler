package com.web.crawler;

import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlerApplication {

	public static void main(String[] args) throws IOException, InvalidFormatException {
		SpringApplication.run(CrawlerApplication.class, args);

		WebCrawlerWithDepth webCrawlerWithDepth = new WebCrawlerWithDepth();
		webCrawlerWithDepth.getPageLinks(args[0]);
		webCrawlerWithDepth.write();
	}

}
