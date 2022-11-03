package com.willhill;

import com.willhill.api.ApiRequest;
import com.willhill.config.SpringConfiguration;
import com.willhill.excel.ExcelFileReader;
import com.willhill.excel.ExcelFileWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) throws Exception {
        IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);

        ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfiguration.class);

        ExcelFileReader excelFileReader = (ExcelFileReader) ctx.getBean("excelFileReader");
        ExcelFileWriter excelFileWriter = (ExcelFileWriter) ctx.getBean("excelFileWriter");
        ExcelFileReader excelFileReaderOfActiveBonuses = (ExcelFileReader) ctx.getBean("excelFileReaderOfActiveBonuses");
        ApiRequest apiRequest = (ApiRequest) ctx.getBean("apiRequest");

        String lastAccountNumber = null;
        for (Row row : excelFileReader.sheet()) {
            if (row.getRowNum() == 0) {
                continue;
            }

            String accountNumber = excelFileReader.getAccountNumberFromExcel(row);

            if (accountNumber == null) {
                break;
            }

            if (accountNumber.equals(lastAccountNumber)) {
                excelFileWriter.updateRow(accountNumber,
                    apiRequest.createRequestFor(accountNumber).getResponse().getBalanceField());
                continue;
            }

            if (excelFileReaderOfActiveBonuses.isAccountNumberInTheList(accountNumber)) {
                excelFileWriter.insertRow(accountNumber,
                    apiRequest.createRequestFor(accountNumber).getResponse().getBalanceField());
            }

            lastAccountNumber = accountNumber;
        }

        excelFileWriter.insertRow("", ApiRequest.getSumOfBalance());
        excelFileWriter.generateExcelFile();
    }
}
