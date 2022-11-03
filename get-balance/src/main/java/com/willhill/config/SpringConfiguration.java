package com.willhill.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.willhill.api.ApiRequest;
import com.willhill.excel.ExcelFileReader;
import com.willhill.excel.ExcelFileWriter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class SpringConfiguration {

    @Bean
    public ExcelFileReader excelFileReader(@Value("${file.read}") String path,
                                           @Value("${column.name}") String columnName)
        throws Exception {

        FileInputStream excelFilePath = new FileInputStream(path);
        Workbook workbook = WorkbookFactory.create(excelFilePath);
        Sheet sheet = workbook.getSheetAt(0);

        return new ExcelFileReader(excelFilePath, workbook, sheet, columnName);
    }

    @Bean
    public ExcelFileReader excelFileReaderOfActiveBonuses(@Value("${file.read.criteria}") String path,
                                                          @Value("${column.name}") String columnName)
        throws Exception {

        FileInputStream excelFilePath = new FileInputStream(path);
        Workbook workbook = WorkbookFactory.create(excelFilePath);
        Sheet sheet = workbook.getSheetAt(0);

        return new ExcelFileReader(excelFilePath, workbook, sheet, columnName);
    }

    @Bean
    public ExcelFileWriter excelFileWriter(@Value("${file.write}") String path,
                                           @Value("${generated.sheet.name}") String sheetName) throws Exception {
        Workbook workbook = new XSSFWorkbook();
        FileOutputStream excelFilePath = new FileOutputStream(path);
        Sheet sheet = workbook.createSheet(sheetName);

        return new ExcelFileWriter(excelFilePath, workbook, sheet);
    }

    @Bean
    public ApiRequest apiRequest(@Value("${request.link}") String link) {
        return new ApiRequest(link);
    }
}
