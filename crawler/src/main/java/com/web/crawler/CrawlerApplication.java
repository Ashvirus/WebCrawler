package com.web.crawler;

import java.io.IOException;
import java.net.URL;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlerApplication {

	public static void main(String[] args) throws IOException, InvalidFormatException {
		SpringApplication.run(CrawlerApplication.class, args);

		WebCrawlerWithDepth webCrawlerWithDepth = new WebCrawlerWithDepth();
		String baseUrl = args[0];
		URL url=new URL(baseUrl);
		String authority = url.getAuthority();
		int indexOf = authority.indexOf('.');
		int lastIndexOf = authority.lastIndexOf('.');
		String substring = authority.substring(indexOf+1, lastIndexOf);
		webCrawlerWithDepth.setDomainName(substring);
		webCrawlerWithDepth.getPageLinks(args[0]);
		webCrawlerWithDepth.write();
	}

}
