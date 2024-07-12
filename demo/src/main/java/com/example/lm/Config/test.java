package com.example.lm.Config;

import org.marc4j.MarcReader;
import org.marc4j.MarcStreamReader;
import org.marc4j.marc.DataField;
import org.marc4j.marc.Record;
import org.marc4j.marc.Subfield;
import org.marc4j.marc.VariableField;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        String marcFilePath = "C://Users//PC//Downloads//IGI ebook MARC.mrc";

        // 定义所有需要查找的字段
        String[] fieldNums = {"245", "100", "110", "700", "710", "490", "041", "020", "264", "260", "250", "856", "300", "650", "520", "505"};

        try (InputStream input = new FileInputStream(marcFilePath)) {
            MarcReader reader = new MarcStreamReader(input);
            while (reader.hasNext()) {
                Record record = reader.next();

                // 存储字段和子字段数据的Map
                Map<String, Map<Character, List<String>>> fieldsMap = new HashMap<>();

                for (String fieldNum : fieldNums) {
                    List<VariableField> fields = record.getVariableFields(fieldNum);

                    for (VariableField field : fields) {
                        if (field instanceof DataField) {
                            DataField dataField = (DataField) field;
                            Map<Character, List<String>> subfieldsMap = fieldsMap.computeIfAbsent(fieldNum, k -> new HashMap<>());

                            for (Subfield subfield : dataField.getSubfields()) {
                                char subfieldCode = subfield.getCode();
                                String subfieldData = subfield.getData();
                                // 仅对非856字段进行字符替换和修剪
                                if (!"856".equals(fieldNum)) {
                                    subfieldData = subfieldData.replaceAll("[/,:]", "").trim();
                                } else {
                                    subfieldData = subfieldData.trim();
                                }
                                subfieldsMap.computeIfAbsent(subfieldCode, k -> new ArrayList<>()).add(subfieldData);
                            }
                        }
                    }
                }

                // 打印结果
                for (Map.Entry<String, Map<Character, List<String>>> fieldEntry : fieldsMap.entrySet()) {
                    String fieldNum = fieldEntry.getKey();
                    Map<Character, List<String>> subfieldsMap = fieldEntry.getValue();
                    System.out.println("Field: " + fieldNum);

                    for (Map.Entry<Character, List<String>> subfieldEntry : subfieldsMap.entrySet()) {
                        char subfieldCode = subfieldEntry.getKey();
                        List<String> subfieldDataList = subfieldEntry.getValue();
                        System.out.println("  Subfield " + subfieldCode + ": " + String.join("; ", subfieldDataList));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
