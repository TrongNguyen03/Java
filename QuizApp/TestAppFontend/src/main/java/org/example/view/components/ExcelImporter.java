package org.example.view.components;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.Question;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelImporter {

    public static List<Question> readQuestionsFromExcel(File file) throws Exception {
        List<Question> questions = new ArrayList<>();

        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Question q = new Question();
            q.setContent(getCellValue(row.getCell(0)));
            List<String> options = new ArrayList<>();
            options.add(getCellValue(row.getCell(1)));
            options.add(getCellValue(row.getCell(2)));
            options.add(getCellValue(row.getCell(3)));
            options.add(getCellValue(row.getCell(4)));
            q.setOptions(options);
            q.setCorrectAnswer(getCellValue(row.getCell(5)));

            questions.add(q);
        }

        workbook.close();
        fis.close();
        return questions;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}
