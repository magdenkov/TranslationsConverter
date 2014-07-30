import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
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

    public static final String DST_XLS_FILE = "d:\\translaadwewewew.xls";
    public static final String SRC_FOLDER = "d:\\trans";


    public static void main(String[] args) throws IOException {
        String [] files = new  String[20];
        files[0] =  "d:/en.js";
        files[1] =  "d:/fr.js";
        files[2] =  "d:/de.js";

        File folder = new File(SRC_FOLDER);
        File[] listOfFiles;
        if (!folder.isDirectory()) {
             return;
        }

        listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Sheet1");

        List <String> languageKeys = new LinkedList<String>();
        Row row1 = sheet.createRow(0);
        for (String f : files) {
            if (f == null) {
                break;
            }
            languageKeys.add(f.substring(f.length() - 6, f.length() - 1));
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

                    if (s.startsWith("//")) {
                        constTranslations.put(s.replaceAll("//", ""), null);
                        continue;
                    }
                    if (s.contains("translations") || s.contains("}")) {
                        continue;
                    }
                    if (s.contains(":")) {
                        String[] words = s.split(":");
                        words[1].replaceAll(",\"", "");
                        constTranslations.put(words[0], new String[3]);
                    }
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

        int rownum = 1;
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
        }

        FileOutputStream out = new FileOutputStream(new File(DST_XLS_FILE));
        workbook.write(out);
        out.close();
        System.out.println("Excel written successfully..");

    }

}




