package com.web.crawler.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.web.crawler.model.DataModelExcel;
import com.web.crawler.service.WebCrawlerService;
import com.web.crawler.service.WriteExcel;

@RestController
public class Controller {

	@Autowired
	private WebCrawlerService webCrawlerWithDepth;
	@Autowired
	private WriteExcel writeExcel;

	@GetMapping("links")
	public String helloWithParm(@RequestParam("name") String input) throws IOException {
		String baseUrl = HtmlUtils.htmlEscape(input.trim());
		URL url = new URL(baseUrl);
		String authority = url.getAuthority();
		int indexOf = authority.indexOf('.');
		int lastIndexOf = authority.lastIndexOf('.');
		String domain = authority.substring(indexOf + 1, lastIndexOf);
		webCrawlerWithDepth.setDomainName(domain);
		Map<Integer, DataModelExcel> pageLinks = webCrawlerWithDepth.getPageLinks(baseUrl);
		writeExcel.writeMap(pageLinks);
		writeExcel.writeError();
		return "Please check the " + domain + ".xlsx file along with " + domain + "Errors.xlsx file";
	}

}
