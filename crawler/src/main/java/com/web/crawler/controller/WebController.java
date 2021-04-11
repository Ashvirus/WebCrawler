package com.web.crawler.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import com.web.crawler.model.DataModelExcel;
import com.web.crawler.service.WebCrawlerService;
import com.web.crawler.service.WriteExcel;

@Controller
public class WebController {

	@Autowired
	private WebCrawlerService webCrawlerWithDepth;
	@Autowired
	private WriteExcel writeExcel;

	@GetMapping("/welcome")
	public String loginMessage() {
		return "welcome";
	}

	@PostMapping("/welcome")
	public String helloWithParm(@RequestParam("Url") String input, ModelMap model, Locale locale) throws IOException {

		String baseUrl = HtmlUtils.htmlEscape(input.trim());
		URL url = new URL(baseUrl);
		String authority = url.getAuthority();
		int indexOf = authority.indexOf('.');
		int lastIndexOf = authority.lastIndexOf('.');
		String domain = authority.substring(indexOf + 1, lastIndexOf);
		webCrawlerWithDepth.setDomainName(domain);
		Map<Integer, DataModelExcel> pageLinks = webCrawlerWithDepth.getPageLinks(baseUrl);
		Collection<DataModelExcel> values = pageLinks.values();

		/*
		 * Collection<DataModelExcel> values = new ArrayList<>(); DataModelExcel
		 * dataModelExcel = new DataModelExcel();
		 * dataModelExcel.setDescription("description 1");
		 * dataModelExcel.setTitle("title1"); dataModelExcel.setRobots("robot1");
		 * dataModelExcel.setUrl(input); values.add(dataModelExcel); DataModelExcel
		 * dataModelExcel2 = new DataModelExcel();
		 * 
		 * dataModelExcel2.setDescription("description 1");
		 * dataModelExcel2.setTitle("title1"); dataModelExcel2.setRobots("robot1");
		 * dataModelExcel2.setUrl(input+"/contact"); values.add(dataModelExcel);
		 */
		model.put("list", values);
		return "display";
	}
}
