package com.web.crawler.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.web.crawler.model.Constant;
import com.web.crawler.model.DataModelExcel;

@Configuration
public class WebCrawlerService {
	private HashSet<String> links;
	private static Logger logger = LoggerFactory.getLogger(WebCrawlerService.class);

	Map<Integer, DataModelExcel> map = new ConcurrentHashMap<Integer, DataModelExcel>(500, 0.8f, 10);

	Map<String, String> errorMap = new ConcurrentHashMap<String, String>(100, 0.8f, 10);
	private int rowNumber = 1;

	public WebCrawlerService() throws InvalidFormatException, IOException {
		links = new HashSet<>();
	}

	private String domainName;

	public String getDomainName() {
		return domainName;
	}

	public Map<String, String> getErrorMap() {
		return errorMap;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public Map<Integer, DataModelExcel> getPageLinks(String url) throws IOException {
		if ((!links.contains(url))) {
			try {

				links.add(url);
				DataModelExcel model = new DataModelExcel();
				Document document = Jsoup.connect(url).get();
				Elements linksOnPage = document.select("a[href]");
				String title = document.title();
				Element first = document.select(Constant.META_NAME + Constant.DESCRIPTION + "]").first();
				String description = first != null ? first.attr("content") : "";
				Element first2 = document.select(Constant.META_NAME + Constant.ROBOTS + "]").first();
				String robots = first2 != null ? first2.attr("content") : "";
				logger.info("Title: {} \tDescription: {} \tRobots: {} \tURL: {} ", title, description, robots, url);
				model.setTitle(title);
				model.setDescription(description);
				model.setRobots(robots);
				model.setUrl(url);
				map.put(rowNumber, model);
				rowNumber++;
				for (Element page : linksOnPage) {
					String attr = page.attr("abs:href");
					if (attr.contains(this.domainName) && (!attr.contains("?")) && (!attr.contains("#")))
						getPageLinks(attr);

				}

			} catch (IOException e) {
				errorMap.put(url, e.getLocalizedMessage());
				logger.error("Exception for URL: {}, message: {}", url, e.getLocalizedMessage());
			}
		}
		return map;
	}

}