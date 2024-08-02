package com.example.lm.Config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class testExcel {

    public static void main(String[] args) {
        try {
            // 创建工作簿对象
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\PC\\Desktop\\IGI ebook Excel.xlsx"));
            // 获取工作簿下sheet的个数
            int sheetNum = xssfWorkbook.getNumberOfSheets();
            System.out.println("该excel文件中总共有：" + sheetNum + "个sheet");

            for (int i = 0; i < sheetNum; i++) {
                System.out.println("读取第" + (i + 1) + "个sheet");
                XSSFSheet sheet = xssfWorkbook.getSheetAt(i);
                // 获取最后一行的num，即总行数。此处从0开始
                int maxRow = sheet.getLastRowNum();

                // 读取表头行
                Map<String, Integer> headerMap = new HashMap<>();
                for (int col = 0; col < sheet.getRow(0).getLastCellNum(); col++) {
                    headerMap.put(sheet.getRow(0).getCell(col).getStringCellValue(), col);
                }

                // 遍历数据行
                for (int row = 1; row <= maxRow; row++) {
                    if (sheet.getRow(row) == null) {
                        continue; // 跳过空行
                    }

                    System.out.println("--------第" + row + "行的数据如下--------");

                    for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
                        String columnName = entry.getKey();
                        int colIndex = entry.getValue();

                        if (sheet.getRow(row).getCell(colIndex) != null) {
                            switch (sheet.getRow(row).getCell(colIndex).getCellType()) {
                                case STRING:
                                    System.out.println(columnName + ": " + sheet.getRow(row).getCell(colIndex).getStringCellValue());
                                    break;
                                case NUMERIC:
                                    System.out.println(columnName + ": " + sheet.getRow(row).getCell(colIndex).getNumericCellValue());
                                    break;
                                case BOOLEAN:
                                    System.out.println(columnName + ": " + sheet.getRow(row).getCell(colIndex).getBooleanCellValue());
                                    break;
                                default:
                                    System.out.println(columnName + ": " + sheet.getRow(row).getCell(colIndex).toString());
                                    break;
                            }
                        } else {
                            System.out.println(columnName + ": 空");
                        }
                    }
                    System.out.println();
                }
            }
            xssfWorkbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
