package com.example.dhanamcollectionsample.playground;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExcelReaderTask implements Runnable {
    private final XSSFSheet sheet;
    private final int startRow;
    private final int endRow;

    public ExcelReaderTask(XSSFSheet sheet, int startRow, int endRow) {
        this.sheet = sheet;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        for (int i = startRow; i < endRow; i++) {
            Row row = sheet.getRow(i);
            // Process the row as needed
            // System.out.println("Processing row: " + row.getCell(0).getStringCellValue());
        }
    }
}
