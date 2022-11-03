package com.willhill.excel;

import java.io.FileOutputStream;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;

@Slf4j
public record ExcelFileWriter(FileOutputStream outputStream, Workbook workbook, Sheet sheet) {

    private static int lastRowNum = 0;

    public void generateExcelFile() throws Exception {
        workbook.write(outputStream);
    }

    public void insertRow(String accountNumber, Double sumOfBonusBalances) {
        if (lastRowNum == 0) {
            insertFirstRow();
        }

        XSSFRow row = (XSSFRow) sheet.createRow(lastRowNum++);
        row.createCell(0).setCellValue(accountNumber);
        row.createCell(1).setCellValue(sumOfBonusBalances);
        log.info("Inserted row: {} {}", accountNumber, sumOfBonusBalances);
    }

    public void updateRow(String accountNumber, Double sumOfBonusBalances) {
        sheet.getRow(sheet.getLastRowNum()).createCell(0).setCellValue(accountNumber);
        Double lastCellBalance = sheet.getRow(sheet.getLastRowNum()).getCell(1).getNumericCellValue();
        sheet.getRow(sheet.getLastRowNum()).createCell(1).setCellValue(lastCellBalance + sumOfBonusBalances);
        log.info("Updated row: {} {}", accountNumber, lastCellBalance + sumOfBonusBalances);
    }

    public void insertFirstRow() {
        XSSFRow row = (XSSFRow) sheet.createRow(lastRowNum++);
        row.createCell(0).setCellValue("accountNumber");
        row.createCell(1).setCellValue("balance");
    }
}
