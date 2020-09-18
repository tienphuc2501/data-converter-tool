package com.lexisnexis.mvr.model;

import com.lexisnexis.mvr.config.CsvColumn;
import com.lexisnexis.mvr.config.RecordField;
import com.lexisnexis.mvr.config.RecordType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrdUtils {
    private static final String EMPTY_STRING = "";
    public static List<Map<String,String>> generateFromOrdDataWithConfig(String pathOrdData,List<RecordType> recordTypes,List<CsvColumn> csvColumns){
        // get all rows that <># from file data.ord
        List<String> rowsOrd = getOrdData(pathOrdData);

        // loop through rows, fill data that match config to each column
        return rowsOrd.stream()
                    .map(ordRow->OrdUtils.getMatchRecordCode(ordRow,recordTypes,csvColumns))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
    }

    private static List<String> getOrdData(String pathOrdData){
        try (Stream<String> stream = Files.lines(Paths.get(pathOrdData))) {
            //1. filter line <> #
            //2. convert it into a List
            return stream
                    .filter(line -> !line.startsWith("#"))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static Map<String,String> getMatchRecordCode(String line, List<RecordType> recordTypes, List<CsvColumn> columns){
        if(line.isEmpty()){
            return null;
        }

        String firstCharacter = line.substring(0,1);

        RecordType recordType = getRecord(firstCharacter,recordTypes);
        if(recordType==null){
            return null;
        }
        Map<String, String> columnsWithValue=null;
        try {
            columnsWithValue = columns.stream().collect(HashMap::new,(m,v)-> m.put(v.getName(), ""),HashMap::putAll);
        }catch (Exception e){
            e.getStackTrace();
        }

        fillValueToColumns(line,recordType.getRecordFields(),columnsWithValue);
        return columnsWithValue;
    }
    
    private static RecordType getRecord(String recordCode,List<RecordType> recordTypes){
        try {
            return recordTypes.stream()
                    .filter(rt -> rt.getRecordCode().equalsIgnoreCase(recordCode))
                    .findFirst().orElse(null);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static void fillValueToColumns(String line, List<RecordField> recordFields, Map<String,String> columnsWithValue){
        for (RecordField recordField:recordFields){
            String columnName = recordField.getName();
            if(columnsWithValue.containsKey(columnName)){
                String columnValue = getValue(line,recordField.getOffset(),recordField.getOffset()+recordField.getLength());
                if(columnName!=null) {
                    columnsWithValue.replace(columnName, columnValue);
                }
            }
        }

        //recordFields.stream()
    }

    private static String getValue(String line,int start,int end){
        int lineLength = line.length();
        try{
            if(start>lineLength){
                return EMPTY_STRING;
            }

            return line.substring(start,end>lineLength?lineLength:end-1);
        }catch(IndexOutOfBoundsException e){
            System.out.println(start+":"+end+":"+lineLength+":"+line);
            e.printStackTrace();
        }
        return null;
    }
}
