import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Jsons2Xls {

    public static final String DISTANTION_XLS_FILE = "d:\\vqadmintranslation.xls";
 //   public static final String SRC_FOLDER = "d:\\trans";


    public static void main(String[] args) throws IOException {
        String [] files = new  String[20];
        files[0] =  "C:/nginx/html/VQAdmin/translations/en.js";
        files[1] =  "C:/nginx/html/VQAdmin/translations/fr.js";
        files[2] =  "C:/nginx/html/VQAdmin/translations/de.js";
       // files[2] =  "d:/de.js";

        /*File folder = new File(SRC_FOLDER);
        File[] listOfFiles;
        if (!folder.isDirectory()) {
             return;
        }

        listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }*/

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");

        List <String> languageKeys = new LinkedList<String>();
        Row row1 = sheet.createRow(0);
        for (String f : files) {
            if (f == null) {
                break;
            }
            languageKeys.add(f.substring(f.length() - 5, f.length() - 3));
        }
        // here wite en fr de

        Map<String, String[]> constTranslations = new LinkedHashMap<String, String[]>();

        int ii = 0;
        boolean firstTime = true;
        for (String f : files) {
            if (f == null) {
                break;
            }
            Path path = Paths.get(f);
            List<String> stringsInFile = Files.readAllLines(path, Charset.forName("UTF-8"));
            if (firstTime) {
                for (String s : stringsInFile) {

                    if (s.trim().startsWith("//")) {
                        constTranslations.put(s.replaceAll("//", ""), null);
                        continue;
                    }
                    if (s.contains("translations") || s.contains("}")) {
                        continue;
                    }
                    if (s.contains(":")) {
                        String[] words = s.split(":");
                        words[0] = words[0].trim();
                        words[1].replaceAll(",\"", "").trim();
                        constTranslations.put(words[0].trim(), new String[3]);
                    }
                }
                firstTime = false;
            }


            for (String s : stringsInFile) {

                if (s.contains("translations") || s.contains("}") || s.contains("//")) {
                    continue;
                }

                String[] words = s.split(":");
                words[0] = words[0].trim();
                
                if (words.length > 1) {
                    words[1] = words[1].replaceAll(",", "").trim();
                    words[1] = words[1].replaceAll("\"", "").trim();
                    System.out.println(words[1] + words[0]);
                    constTranslations.get(words[0])[ii] = words[1];
                }
            }
            ii++;
        }

        for (String name: constTranslations.keySet()){
            String [] value = constTranslations.get(name);
            StringBuffer result = new StringBuffer();
            if (value != null) {
                for (int i = 0; i < value.length; i++) {
                    result.append(value[i]);
                }
                String mynewstring = result.toString();
                System.out.println(name + " " + mynewstring);
            }
        }
        
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        DataFormat df = workbook.createDataFormat();

        
        CellStyle style;
        Font headerFont = workbook.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 12);
        
        style = createBorderedStyle(workbook);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(headerFont);
        style.setFillForegroundColor(HSSFColor.AQUA.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styles.put("styleLang", style);
        
        style = createBorderedStyle(workbook);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(headerFont);
        style.setFillForegroundColor(HSSFColor.ORANGE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styles.put("styleHeader", style);
        

        style = createBorderedStyle(workbook);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("styleKey", style);
        

        style = createBorderedStyle(workbook);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("styleValue0", style);
        

        style = createBorderedStyle(workbook);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("styleValue1", style);
        
        style = createBorderedStyle(workbook);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("styleValue2", style);

        int rownum = 0;
        Row row = sheet.createRow(rownum++); 
        for (int i = 0; i < languageKeys.size(); i++) {
            String lang = languageKeys.get(i);
          
            Cell cell = row.createCell(i + 1);
            cell.setCellStyle(styles.get("styleLang"));
            cell.setCellValue(lang);
        }
        
        for (String s: constTranslations.keySet()) {

            row = sheet.createRow(rownum++);

            String [] values = constTranslations.get(s);

            Cell cell = row.createCell(0);
 
            if (values == null) {
                cell.setCellStyle(styles.get("styleHeader"));
                sheet.addMergedRegion(new CellRangeAddress(rownum - 1,rownum - 1, 0, languageKeys.size()));
            } else {
                cell.setCellStyle(styles.get("styleKey"));
            }
            
            cell.setCellValue(s);
            
            
            
            int cellNum = 1;
            if (values == null) {
                continue;
            }
            for (int i = 0; i < values.length; i++) {
                String ss = values[i];
                
                if (ss != null) {
                    cell = row.createCell(cellNum);
                    cell.setCellStyle(styles.get("styleValue" + i));
                    cell.setCellValue(ss);
                    cellNum++;
                }
            }
        }
        
        HSSFRow row2 = workbook.getSheetAt(0).getRow(0);
        for (int colNum = 0; colNum < row2.getLastCellNum(); colNum++)   
            workbook.getSheetAt(0).autoSizeColumn(colNum);

        FileOutputStream out = new FileOutputStream(new File(DISTANTION_XLS_FILE));
        workbook.write(out);
        out.close();
        System.out.println("Excel written successfully..");

    }

    
    private static CellStyle createBorderedStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }
}




