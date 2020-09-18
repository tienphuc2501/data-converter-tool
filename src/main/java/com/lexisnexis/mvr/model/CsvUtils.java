package com.lexisnexis.mvr.model;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;

public class CsvUtils {

    //google->search: convert list of map to csv java
    private static List<String[]> toStringCSV(List<Map<String, String>> list){
        List<String[]> data = new ArrayList<>();

        List<String> headers = list.stream().flatMap(map -> map.keySet().stream()).distinct().collect(toList());
        data.add(headers.toArray(new String[0]));
        /*
        for (int i = 0; i < headers.size(); i++) {
            data.add(headers.get(i));
        }*/

        list.forEach(colum-> data.add(colum.entrySet().stream().map(Map.Entry::getValue).toArray(String[]::new)));
        /*
        for (Map<String, String> map : list) {
            List<String> rowItem = new ArrayList<>();
            for (int i = 0; i < headers.size(); i++) {
                rowItem.add(map.get(headers.get(i)));
            }
            data.add(rowItem.toArray(new String[0]));
        }*/

        return data;
    }

    public static void CreateCSVFile(String pathCsvFile,List<Map<String,String>> data){
        if(pathCsvFile.isEmpty()){
            return;
        }

        List<String[]> dataCsv = toStringCSV(data);
        try{
            Path myPath= Paths.get(pathCsvFile);

            try (CSVWriter writer = new CSVWriter(Files.newBufferedWriter(myPath,
                    StandardCharsets.UTF_8), CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END)) {

                writer.writeAll(dataCsv, true);
            }
        }catch (InvalidPathException | IOException e){
            e.printStackTrace();
        }
    }
}
