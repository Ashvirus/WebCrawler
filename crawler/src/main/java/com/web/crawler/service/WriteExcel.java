package com.web.crawler.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.crawler.model.Constant;
import com.web.crawler.model.DataModelExcel;

@Service
public class WriteExcel {
	private static Logger logger = LoggerFactory.getLogger(WriteExcel.class);
	@Autowired
	WebCrawlerWithDepth webCrawlerWithDepth;

	public void write(Map<Integer, DataModelExcel> map) throws IOException {
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		String domainName = webCrawlerWithDepth.getDomainName();
		String fileLocation = path.substring(0, path.length() - 1) + domainName + ".xlsx";

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet(domainName.toUpperCase());
			sheet.setColumnWidth(0, 10000);
			sheet.setColumnWidth(1, 6000);
			sheet.setColumnWidth(2, 10000);
			sheet.setColumnWidth(3, 10000);

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
			headerCell.setCellValue(Constant.ExcelCellName.TITLE.name());
			headerCell.setCellStyle(headerStyle);

			headerCell = header.createCell(1);
			headerCell.setCellValue(Constant.ExcelCellName.DESCRIPTION.name());
			headerCell.setCellStyle(headerStyle);

			headerCell = header.createCell(2);
			headerCell.setCellValue(Constant.ExcelCellName.META_ROBOTS.name());
			headerCell.setCellStyle(headerStyle);

			headerCell = header.createCell(3);
			headerCell.setCellValue(Constant.ExcelCellName.URL.name());
			headerCell.setCellStyle(headerStyle);

			CellStyle style = workbook.createCellStyle();
			style.setWrapText(true);

			map.forEach((k, v) -> {
				Row row = sheet.createRow(k);
				Cell cell = row.createCell(0);
				cell.setCellValue(v.getTitle());

				cell = row.createCell(1);
				cell.setCellValue(v.getDescription());

				cell = row.createCell(2);
				cell.setCellValue(v.getRobots());

				cell = row.createCell(3);
				cell.setCellValue(v.getUrl());
				cell.setCellStyle(style);
			});
			FileOutputStream outputStream = new FileOutputStream(fileLocation);
			workbook.write(outputStream);
			outputStream.close();

		} catch (Exception e) {
			logger.error("Error writing into excel file: {}", e);
		}

	}

}
