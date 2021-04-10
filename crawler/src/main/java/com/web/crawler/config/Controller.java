package com.web.crawler.config;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.web.crawler.model.DataModelExcel;
import com.web.crawler.service.WebCrawlerWithDepth;
import com.web.crawler.service.WriteExcel;

@RestController
public class Controller {

	@Autowired
	WebCrawlerWithDepth webCrawlerWithDepth;
	@Autowired
	WriteExcel writeExcel;

	@GetMapping("links")
	public String helloWithParm(@RequestParam("name") String input) throws IOException {
		String baseUrl = HtmlUtils.htmlEscape(input.trim());
		URL url = new URL(baseUrl);
		String authority = url.getAuthority();
		int indexOf = authority.indexOf('.');
		int lastIndexOf = authority.lastIndexOf('.');
		String substring = authority.substring(indexOf + 1, lastIndexOf);
		webCrawlerWithDepth.setDomainName(substring);
		Map<Integer, DataModelExcel> pageLinks = webCrawlerWithDepth.getPageLinks(baseUrl);
		writeExcel.write(pageLinks);
		return "Please check the crawler.xlsx file";
	}
}
