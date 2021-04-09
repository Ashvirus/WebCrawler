package com.web.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebCrawlerWithDepth {
	private HashSet<String> links;
	private static Logger logger = LoggerFactory.getLogger(WebCrawlerWithDepth.class);
	Workbook workbook;
	Sheet sheet;
	CellStyle style;
	String fileLocation;
	HashMap<Integer, Model> map = new HashMap<Integer, Model>();
	public static int rowNumber = 1;

	public WebCrawlerWithDepth() throws InvalidFormatException, IOException {
		links = new HashSet<>();
	}

	private static String domainName;
	
	public static String getDomainName() {
		return domainName;
	}

	public static void setDomainName(String domainName) {
		WebCrawlerWithDepth.domainName = domainName;
	}

	public void getPageLinks(String URL) throws IOException {
		if ((!links.contains(URL))) {
			try {

				links.add(URL);
				Model model = new Model();
				Document document = Jsoup.connect(URL).get();
				Elements linksOnPage = document.select("a[href]");
				String title = document.title();
				Element first = document.select(Constant.META_NAME + Constant.DESCRIPTION + "]").first();
				String description = first != null ? first.attr("content") : "";
				Element first2 = document.select(Constant.META_NAME + Constant.ROBOTS + "]").first();
				String robots = first2 != null ? first2.attr("content") : "";
				 logger.info("Title: {} \tDescription: {} \tRobots: {} \tURL: {} ", title,
				 description, robots, URL);
				model.title = title;
				model.description = description;
				model.robots = robots;
				model.url = URL;
				map.put(rowNumber, model);
				rowNumber++;
				for (Element page : linksOnPage) {
					String attr = page.attr("abs:href");
					if (attr.contains(this.domainName))
						getPageLinks(attr);

				}

			} catch (IOException e) {
				logger.error("Exception for URL: {}, message: {}", URL, e.getLocalizedMessage());
			}
		}
	}

	public void write() throws IOException {
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		fileLocation = path.substring(0, path.length() - 1) + "crawler.xlsx";

		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Crawler");
		sheet.setColumnWidth(0, 4000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 2000);
		sheet.setColumnWidth(3, 4000);

		Row header = sheet.createRow(0);

		CellStyle headerStyle = workbook.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short) 16);
		font.setBold(true);
		headerStyle.setFont(font);

		Cell headerCell = header.createCell(0);
		headerCell.setCellValue("Title");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(1);
		headerCell.setCellValue("Description");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(2);
		headerCell.setCellValue("Robots");
		headerCell.setCellStyle(headerStyle);

		headerCell = header.createCell(3);
		headerCell.setCellValue("URL");
		headerCell.setCellStyle(headerStyle);

		style = workbook.createCellStyle();
		style.setWrapText(true);

		map.forEach((k, v) -> {
			Row row = sheet.createRow(k);
			Cell cell = row.createCell(0);
			cell.setCellValue(v.title);

			cell = row.createCell(1);
			cell.setCellValue(v.description);

			cell = row.createCell(2);
			cell.setCellValue(v.robots);

			cell = row.createCell(3);
			cell.setCellValue(v.url);
			cell.setCellStyle(style);
		});

		FileOutputStream outputStream = new FileOutputStream(fileLocation);
		workbook.write(outputStream);
		outputStream.close();
		workbook.close();

	}

	
	class Model {
		public String title;
		public String description;
		public String robots;
		public String url;

	}
}