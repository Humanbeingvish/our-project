package com.cognicx.AppointmentRemainder.util;


import java.io.IOException;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

public  class ExcelUtil {
	String currentDirectory = System.getProperty("user.dir");
	final String fileName = currentDirectory+"\\"+"Excels.xlsx";
	XSSFWorkbook workbook = new XSSFWorkbook();
	
	public Workbook getWorkbook() {
		return workbook;
	}
	
	public XSSFSheet newWorkbook() {
		XSSFSheet sheet = workbook.createSheet("sheet");
		return sheet;
	}
	
	public CellStyle getStyle() {
		CellStyle style = workbook.createCellStyle();  
		style.setBorderBottom(BorderStyle.THICK);  
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;
	}
	
	public CellStyle getStyle1() {
		CellStyle style1 = workbook.createCellStyle();  
		style1.setBorderRight(BorderStyle.THICK);  
		style1.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style1.setAlignment(HorizontalAlignment.CENTER);
		return style1;
	}
	
	
	public CellStyle getStyle2() {
		CellStyle style2 = workbook.createCellStyle();  
		style2.setBorderBottom(BorderStyle.THIN);  
		style2.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style2.setBorderRight(BorderStyle.MEDIUM);  
		style2.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style2.setAlignment(HorizontalAlignment.CENTER);
		return style2;
	}
	
	public CellStyle getFontStyle() {
		CellStyle fontStyle = workbook.createCellStyle(); 
		XSSFFont my_font=workbook.createFont();
		my_font.setBold(true);
		fontStyle.setFont(my_font);
		fontStyle.setAlignment(HorizontalAlignment.CENTER);
		return fontStyle;
	}
	
	public CellStyle getStyleHeader() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderRight(BorderStyle.THICK);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderTop(BorderStyle.THICK);  
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THICK);  
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		//styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	public static CellStyle getCellStyleForHeader(Workbook workbook) {
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THICK);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.THICK);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.THICK);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.THICK);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setWrapText(true);
		style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;

	}
	
	
	public static CellStyle getCellStyleForRulesetData(Workbook workbook,Boolean isBold) {
		Font headerFont = workbook.createFont();
		headerFont.setBold(isBold);
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.DOTTED);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.DOTTED);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.DOTTED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.DOTTED);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setWrapText(true);
		style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setAlignment(HorizontalAlignment.CENTER);
		return style;

	}
    
	public static CellStyle getCellStyleForContent(Workbook workbook) {
		CellStyle styleContent = workbook.createCellStyle();
		/*
		 * styleContent.setBorderBottom(BorderStyle.THICK);
		 * styleContent.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		 * styleContent.setBorderRight(BorderStyle.THICK);
		 * styleContent.setRightBorderColor(IndexedColors.BLACK.getIndex());
		 * styleContent.setBorderTop(BorderStyle.THICK);
		 * styleContent.setTopBorderColor(IndexedColors.BLACK.getIndex());
		 * styleContent.setBorderLeft(BorderStyle.THICK);
		 * styleContent.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		 */
		styleContent.setAlignment(HorizontalAlignment.CENTER);
		return styleContent;

	}
	
	public static CellStyle getCellStyleRuleTable(Workbook workbook,Boolean isBold) {
		Font headerFont = workbook.createFont();
		headerFont.setBold(isBold);
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.DOTTED);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.DOTTED);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.DOTTED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.DOTTED);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setWrapText(true);
		style.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setAlignment(HorizontalAlignment.LEFT);
		return style;

	}
	
	public static CellStyle getCellStyleHeaderName(Workbook workbook) {
		Font headerFont = workbook.createFont();
		headerFont.setBold(false);
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.DOTTED);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.DOTTED);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.DOTTED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.DOTTED);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setWrapText(true);
		style.setFont(headerFont);
		style.setAlignment(HorizontalAlignment.LEFT);
		return style;

	}
	
	public static CellStyle getCellStyleContent(Workbook workbook,String section) {
		Font headerFont = workbook.createFont();
		headerFont.setBold(false);
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.DOTTED);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderRight(BorderStyle.DOTTED);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(BorderStyle.DOTTED);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(BorderStyle.DOTTED);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setWrapText(true);
//		if(RuleEngineConstants.ACTION.equalsIgnoreCase(section)) 
//			style.setFillForegroundColor(IndexedColors.RED.getIndex());
//		else if(RuleEngineConstants.CONDITION.equalsIgnoreCase(section))
//			style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
//		else if(RuleEngineConstants.NAME.equalsIgnoreCase(section))
			style.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setAlignment(HorizontalAlignment.LEFT);
		return style;

	}
	@SuppressWarnings("deprecation")
	public static void frameMerged(CellRangeAddress region, Sheet sheet, Workbook wb) {
		sheet.addMergedRegion(region);
		final short borderMediumDashed = CellStyle.BORDER_THICK;
		RegionUtil.setBorderBottom(borderMediumDashed, region, sheet, wb);
		RegionUtil.setBorderTop(borderMediumDashed, region, sheet, wb);
		RegionUtil.setBorderLeft(borderMediumDashed, region, sheet, wb);
		RegionUtil.setBorderRight(borderMediumDashed, region, sheet, wb);
	}

	@SuppressWarnings("deprecation")
	public static void setRegionBorderWithMedium(CellRangeAddress region, Sheet sheet, Workbook wb) {
		RegionUtil.setBorderBottom(CellStyle.BORDER_THICK, region, sheet, wb);
		RegionUtil.setBorderLeft(CellStyle.BORDER_THICK, region, sheet, wb);
		RegionUtil.setBorderRight(CellStyle.BORDER_THICK, region, sheet, wb);
		RegionUtil.setBorderTop(CellStyle.BORDER_THICK, region, sheet, wb);
	}
	 
	public static void setCellValue(CellStyle style, Row row, int cellPosition, Double value) {
		Cell cell = row.createCell(cellPosition);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static void setCellValue(CellStyle style, Row row, int cellPosition, int value) {
		Cell cell = row.createCell(cellPosition);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static void setCellValue(CellStyle style, Row row, int cellPosition, String value) {
		Cell cell = row.createCell(cellPosition);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static void setCellValue(CellStyle style, Row row, int cellPosition, long value) {
		Cell cell = row.createCell(cellPosition);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}
	
	public static int getLogo(Workbook workbook, Sheet sheet) throws IOException {
		ClassPathResource headerLogo = new ClassPathResource("excel_header_logo.png");
		byte[] logoBytes = IOUtils.toByteArray(headerLogo.getInputStream());
		return workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG);
	}
	
	public CellStyle getStyleLeftBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderLeft(BorderStyle.THICK);  
		styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderRight(BorderStyle.THICK);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderTop(BorderStyle.THICK);  
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THICK);  
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	
	public CellStyle getStyleLeftBoldWithMediumBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		//my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderLeft(BorderStyle.MEDIUM);  
		styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderRight(BorderStyle.MEDIUM);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderTop(BorderStyle.MEDIUM);  
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.MEDIUM);  
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	public CellStyle getStyleRightThickBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		styleHeader.setBorderRight(BorderStyle.THICK);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THIN);
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	
	public CellStyle getOverAllThinBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		//my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderLeft(BorderStyle.THIN);  
		styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderRight(BorderStyle.THIN);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderTop(BorderStyle.THIN);  
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THIN);  
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	public CellStyle getStyleLeftRightMediumBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		styleHeader.setBorderRight(BorderStyle.MEDIUM);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderLeft(BorderStyle.MEDIUM);
		styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	public CellStyle getStyleOverAllWithoutLeftRightThickBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderTop(BorderStyle.THICK);  
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THICK);  
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	
	public CellStyle getStyleRightBottomThickBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderRight(BorderStyle.THICK);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THICK);
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	public CellStyle getStyleOnlyWithoutLeftAllThickBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderRight(BorderStyle.THICK);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THICK);
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderTop(BorderStyle.THICK);
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	
	public CellStyle getRightThickElseOverAllThinBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		//my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderLeft(BorderStyle.THIN);  
		styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderRight(BorderStyle.THICK);  
		styleHeader.setRightBorderColor(IndexedColors.GREY_80_PERCENT.getIndex());
		styleHeader.setBorderTop(BorderStyle.THIN);  
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THIN);  
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	public CellStyle getOverAllThinBorderWithBoldFont() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderLeft(BorderStyle.THIN);  
		styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderRight(BorderStyle.THIN);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderTop(BorderStyle.THIN);  
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THIN);  
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return styleHeader;
	}
	
	public CellStyle getStyleRightThickElseOtherThin() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderRight(BorderStyle.THICK);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THIN);
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderTop(BorderStyle.THIN);
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderLeft(BorderStyle.THIN);
		styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return styleHeader;
	}
	
	
	public CellStyle getStyleOnlyWithoutRightAllThickBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderLeft(BorderStyle.THICK);  
		styleHeader.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THICK);
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderTop(BorderStyle.THICK);
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	public CellStyle getStyleOnlyRightThickBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		styleHeader.setBorderRight(BorderStyle.THICK);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	public CellStyle getStyleOnlyTopThickBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		styleHeader.setBorderTop(BorderStyle.THICK);  
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}

	public CellStyle getStyleOnlyCenterAlignMent() {
		CellStyle styleHeader = workbook.createCellStyle(); 
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}
	
	
	public CellStyle getStyleOnlyWithoutLeftWithRightTHinBorder() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setBorderRight(BorderStyle.THIN);  
		styleHeader.setRightBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderBottom(BorderStyle.THICK);
		styleHeader.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setBorderTop(BorderStyle.THICK);
		styleHeader.setTopBorderColor(IndexedColors.BLACK.getIndex());
		styleHeader.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styleHeader.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}

	public void fillFooter(Row row, Cell cell, int count, int colNumber) {
		cell = row.createCell(colNumber++);
        cell.setCellValue(count );
        cell.setCellStyle(getStyleOverAllWithoutLeftRightThickBorder());
        
        cell = row.createCell(colNumber++);
        cell.setCellValue("");
        cell.setCellStyle(getStyleOnlyWithoutLeftAllThickBorder());
	}

	
	public CellStyle getBoldFont() {
		CellStyle styleHeader = workbook.createCellStyle();  
		XSSFFont my_font1=workbook.createFont();
		my_font1.setBold(true);
		styleHeader.setFont(my_font1);
		styleHeader.setAlignment(HorizontalAlignment.CENTER);
		return styleHeader;
	}

	public static void addCellWithValue(Row row, Cell cell, int colNum, String label, CellStyle borderStyle) {
		 	cell = row.createCell(colNum++);
		    cell.setCellValue(label);
		    cell.setCellStyle(borderStyle);
	}

	public static void addCellWithValueAndDoubleStyle(Row row, Cell cell, int colNumber, String value,
			CellStyle style1, CellStyle style2) {
		cell = row.createCell(colNumber++);
	    cell.setCellValue(value);
	    cell.setCellStyle(style1);
	    cell.setCellStyle(style2);
	}
	
}
