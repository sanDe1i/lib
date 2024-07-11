package com.example.lm.Config;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class Marc4jTest {
    public static void main(String[] args) {
        try {
            // 指定MARC文件的路径
            String marcFilePath = "C://Users//PC//Downloads//IGI ebook MARC.mrc";
            InputStream input = new FileInputStream(marcFilePath);
            MarcReader reader = new MarcStreamReader(input);
            String fieldNum = "245";
            int bookCount = 0;
            // 读取每条记录
            while (reader.hasNext()) {
                Record record = reader.next();
                if (hasField(record, fieldNum)) {
                    bookCount++;

                }
                // 打印记录中的控制字段001（编号）和其他数据
                String controlNumber = record.getControlNumber();
                List<VariableField> aa = record.find("245", "a");
                VariableField aaa = record.getVariableField("245");
                for(VariableField field : aa) {
                   // System.out.println(field.toString());
                }

                /*// 获取245字段
                VariableField field245 = record.getVariableField("245");
                if (field245 instanceof DataField) {
                    DataField dataField = (DataField) field245;

                    // 提取$a子字段
                    Subfield subfieldA = dataField.getSubfield('a');
                    if (subfieldA != null) {
                        String title = subfieldA.getData().replaceAll("[/,:]", "").trim();
                        System.out.println("Title: " + title);
                    }

                    // 提取$c子字段
                    Subfield subfieldC = dataField.getSubfield('c');
                    if (subfieldC != null) {
                        String responsibility = subfieldC.getData().replaceAll("[/,:]", "").trim();
                        System.out.println("Statement of responsibility: " + responsibility);
                    }
                }*/
              //  System.out.println("Control Number: " + controlNumber);
                System.out.println("Record Data: " + record.toString());
               // System.out.println("File number: " + bookCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean hasField(Record record, String fieldNum) {
        for (VariableField field : record.getVariableFields()) {
            if (field.getTag().equals(fieldNum)) {
                return true;
            }
        }
        return false;
    }
}
