import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class Xls2Jsons {
    private final static String SRC_XLSX = "d://translations.xlsx";
    public static final String DISTANATION_FOLDER = "d://";

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        FileInputStream inp = new FileInputStream( new File(SRC_XLSX ));
        Workbook workbook = WorkbookFactory.create(inp);

        // Get the first Sheet.
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        List<FileJson> jsonsArray = getFileJsons(rowIterator);

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            //For each row, iterate through all the columns
            Iterator<Cell> cellIterator = row.cellIterator();
            List <String> translations = new ArrayList<String>();
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                translations.add(cell.getStringCellValue());
            }

            for (int i = 0; i < jsonsArray.size(); i++){
                if (translations.size() > 1) {
                    FileJson fileJson = jsonsArray.get(i);
                    fileJson.getJson().append(translations.get(0)).
                            append(": \"").append(translations.get(i + 1)).append("\",\n");
                }

                if (translations.size() == 1) {
                    FileJson fileJson = jsonsArray.get(i);
                    fileJson.getJson().append("//").append(translations.get(0)).append("\n");
                }
            }

        }

        for (FileJson fileJson : jsonsArray) {
            System.out.println(fileJson.getJson().toString());
            cretetDirectories();


            String name = DISTANATION_FOLDER + fileJson.getName() + ".js";
            fileJson.getJson().append("}");
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(name), "utf-8"));

            writer.write(fileJson.getJson().toString());
            writer.close();
        }


    }

    private static void cretetDirectories() {
        File dir = new File(DISTANATION_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static List<FileJson> getFileJsons(Iterator<Row> rowIterator) {
        Row row1 = rowIterator.next();
        Iterator<Cell> cellIterator1 = row1.cellIterator();
        int amountOfLanguages = 0;
        List<FileJson> jsonsArray = new ArrayList<FileJson>();
        cellIterator1.next();
        while (cellIterator1.hasNext()) {
            Cell cell = cellIterator1.next();
            amountOfLanguages++;
            jsonsArray.add(new FileJson(cell.getStringCellValue(), new StringBuilder("translations = {\n")));
        }

        checkAmountOfLanguages(amountOfLanguages, jsonsArray);
        return jsonsArray;
    }

    private static void checkAmountOfLanguages(int amountOfLanguages, List<FileJson> jsonsArray) {
        System.out.println("Amount of languages " + amountOfLanguages);
        System.out.println("Will be created files for folowing languages: ");
        for (FileJson fileJson : jsonsArray) {
            System.out.println(fileJson.getName());
        }
    }

}
