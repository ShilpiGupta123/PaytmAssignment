package movies;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteToExcelFile {
	
	public static void writeMoviesNamWithIsContentAvailable0OnExel(String fileName, List<Map<String, String>> movies)
			throws Exception {
		Workbook workbook = null;

		if (fileName.endsWith("xlsx")) {
			workbook = new XSSFWorkbook();
		} else if (fileName.endsWith("xls")) {
			workbook = new HSSFWorkbook();
		} else {
			throw new Exception("invalid file name, should be xls or xlsx");
		}
		Sheet sheet = workbook.createSheet("Movie");

     	// Creating a font
        Font font= workbook.createFont();
        font.setFontHeightInPoints((short)10);
        font.setFontName("Arial");
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setBold(true);
        font.setItalic(false);
 
        CellStyle style = workbook.createCellStyle();;
        style.setFont(font);
	
		int row = 0;
		Cell cell0 = sheet.createRow(row++).createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("Movies");
		for(int i=0; i<movies.size(); i++) {
			String movieName = "";
			for(Map.Entry<String,String> entry : movies.get(i).entrySet()) {
				
				if(entry.getKey()!=null && entry.getKey().equals("provider_moviename")) {
					movieName = entry.getValue();
				}
				if(entry.getKey()!=null  
						&& entry.getKey().equals("isContentAvailable") 
						&& "0".equals(String.valueOf(entry.getValue()))) {
					sheet.createRow(row++).createCell(0).setCellValue(movieName);
				}
			}
		}
		
		
		FileOutputStream fos = new FileOutputStream(fileName);
		workbook.write(fos);
		fos.close();
		System.out.println(fileName + " written successfully");
	}
	
}