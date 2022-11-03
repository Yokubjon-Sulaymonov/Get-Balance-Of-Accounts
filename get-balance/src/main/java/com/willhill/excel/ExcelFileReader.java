package com.willhill.excel;

import java.io.FileInputStream;

import com.willhill.exception.NoSuchColumnException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public record ExcelFileReader(FileInputStream outputStream, Workbook workbook, Sheet sheet, String columnName) {

    public int columnCellIndexOf(String columnName) throws Exception {
        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getStringCellValue().equalsIgnoreCase(columnName)) {
                    return cell.getColumnIndex();
                }
            }
        }
        throw new NoSuchColumnException(String.format("Column %s is not found", columnName));
    }

    public String getAccountNumberFromExcel(Row row) throws Exception {
        int cellIndex = columnCellIndexOf(columnName);
        String accountNumber = null;

        if (row.getCell(cellIndex) == null) {
            return null;
        }

        if (row.getCell(cellIndex).getCellType() == CellType.STRING) {
            accountNumber = row.getCell(cellIndex).getStringCellValue();
        } else if (row.getCell(cellIndex).getCellType() == CellType.NUMERIC) {
            accountNumber = String.valueOf((int) row.getCell(cellIndex).getNumericCellValue());
        }
        return accountNumber;
    }

    public Boolean isAccountNumberInTheList(String accountNumber) throws Exception {
        int cellIndex = columnCellIndexOf("accountNumber");
        String accountNumberFromDatabase = null;

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue;
            }

            if (row.getCell(cellIndex) == null) {
                break;
            }

            if (row.getCell(cellIndex).getCellType() == CellType.STRING) {
                accountNumberFromDatabase = row.getCell(cellIndex).getStringCellValue();
            } else if (row.getCell(cellIndex).getCellType() == CellType.NUMERIC) {
                accountNumberFromDatabase = String.valueOf((int) row.getCell(cellIndex).getNumericCellValue());
            }

            if (accountNumber.equals(accountNumberFromDatabase)) {
                return true;
            }
        }
        return false;
    }
}
