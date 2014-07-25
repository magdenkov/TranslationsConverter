import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by denis.magdenkov on 23.07.2014.
 */
public class Jsons2Xls {

    public static void main(String[] args) throws IOException {
        String [] files = new  String[20];
        files[0] =  "d:/en.js";
        files[1] =  "d:/fr.js";
        files[2] =  "d:/de.js";


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");

        Map<String, String[]> constTranslations = new HashMap<String, String[]>();

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

                    if (s.startsWith("//")) {
                        constTranslations.put(s.replaceAll("//", ""), null);
                        continue;
                    }
                    if (s.contains("translations") || s.contains("}")) {
                        continue;
                    }
                    String[] words = s.split(":");
                    words[1].replaceAll(",\"", "");
                    constTranslations.put(words[0], new String[3]);
                }
                firstTime = false;
            }


            for (String s : stringsInFile) {

                if (s.contains("translations") || s.contains("}") || s.contains("//")) {
                    continue;
                }

                String[] words = s.split(":");
                if (words.length > 1) {
                    words[1] = words[1].replaceAll(",", "");
                    words[1] = words[1].replaceAll("\"", "");
                //    System.out.println(words[1] + words[0]);
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


        int rownum = 0;
        for (String s: constTranslations.keySet()){

            Row row = sheet.createRow(rownum++);

            String [] values = constTranslations.get(s);

            Cell cell = row.createCell(0);
            cell.setCellValue(s);
            int cellNum = 1;
            if (values == null) {
                continue;
            }
            for (String ss : values) {
                if (ss != null) {
                    cell = row.createCell(cellNum);
                    cell.setCellValue(ss);
                    cellNum++;
                }

            }




           /* String[] words = s.split(":");
            words[1].replaceAll(",\"", "");

            Cell cell = row.createCell(0);
            cell.setCellValue(words[0]);
            cell = row.createCell(1);
            cell.setCellValue(words[1]);*/
        }






        FileOutputStream out =
                new FileOutputStream(new File("d:\\translaadwewewew.xls"));
        workbook.write(out);
        out.close();
        System.out.println("Excel written successfully..");




    }

}





 /*  int rownum = 0;
        for (String s : strings) {

            Row row = sheet.createRow(rownum++);
            if (s.startsWith("//")) {
                Cell cell = row.createCell(0);
                cell.setCellValue(s);
                continue;
            }
            if (s.startsWith("translations") || s.startsWith("}")) {
                continue;
            }

            String[] words = s.split(":");
            words[1].replaceAll(",\"", "");

            Cell cell = row.createCell(0);
            cell.setCellValue(words[0]);
            cell = row.createCell(1);
            cell.setCellValue(words[1]);
        }*/


        /*Path path = Paths.get("d:/en.js");


        List<String> strings = Files.readAllLines(path, Charset.forName("UTF-8"));


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");

        int rownum = 0;
        for (String s : strings ){

            Row row = sheet.createRow(rownum++);
            if (s.startsWith("//")) {
                Cell cell = row.createCell(0);
                cell.setCellValue(s);
                continue;
            }
            if (s.startsWith("translations") || s.startsWith("}")) {
                continue;
            }

            String [] words = s.split(":");
            words[1].replaceAll(",\"", "");

            Cell cell = row.createCell(0);
            cell.setCellValue(words[0]);
            cell = row.createCell(1);
            cell.setCellValue(words[1]);
        }*/




    /*    Map<String, Object[]> data = new HashMap<String, Object[]>();
        data.put("1", new Object[] {"Emp No.", "Name", "Salary"});
        data.put("2", new Object[] {1d, "John", 1500000d});
        data.put("3", new Object[] {2d, "Sam", 800000d});
        data.put("4", new Object[] {3d, "Dean", 700000d});

        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if(obj instanceof Date)
                    cell.setCellValue((Date)obj);
                else if(obj instanceof Boolean)
                    cell.setCellValue((Boolean)obj);
                else if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Double)
                    cell.setCellValue((Double)obj);
            }
        }
*/









