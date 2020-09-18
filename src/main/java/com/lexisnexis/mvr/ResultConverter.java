package com.lexisnexis.mvr;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import com.google.gson.reflect.TypeToken;
import com.lexisnexis.mvr.config.CsvColumn;
import com.lexisnexis.mvr.config.RecordType;
import com.lexisnexis.mvr.model.CsvUtils;
import com.lexisnexis.mvr.model.JsonUtils;
import com.lexisnexis.mvr.model.OrdUtils;

public class ResultConverter {
    private static final String ORD_FILE_PATH = "src\\main\\resources\\config-ord.json";
    private static final String CSV_FILE_PATH = "src\\main\\resources\\config-csv.json";
    private static final String DATA_FILE_PATH = "E:\\Projects\\TuLe\\datas";
    private static final String RESULT_CSV_FILE_PATH = "src\\main\\resources\\result.csv";

    private static final String ROOT_NODE_NAME_CSV = "columns";
    private static final String ROOT_NODE_NAME_ORD = "recordLayouts";
    private static final String CSV_EXTENSION = ".csv";
    private static final String ORD_EXTENSION = ".ord";

    // "C:\Program Files\Java\jdk1.8.0_221\bin\javac.exe" src/main/java/com/lexisnexis/mvr/*.java -d classes
    // java E:\Projects\TuLe\target\classes\com\lexisnexis\mvr\ResultConverter E:\\Projects\\TuLe\\datas\\Copy.ord E:\\Projects\\TuLe\\datas\\Copy.csv
    // java ResultConverter E:\\Projects\\TuLe\\datas E:\\Projects\\TuLe\\datacsv
    /*
    * arg[0]: path to file or folder data
    * arg[1]: (Optional) pathfile or folder save generated csv files
    *         If arg[1] empty -> files .csv save the same with folder data
    */
    public static void main(String... args){

        //https://stackoverflow.com/questions/32444863/google-gson-linkedtreemap-class-cast-to-myclass
        Type rcType = new TypeToken<ArrayList<RecordType>>(){}.getType();

        // Convert config-csv.json to Json Object
        List<RecordType> recordTypes = JsonUtils.covertJSONToObject(ORD_FILE_PATH,ROOT_NODE_NAME_ORD,rcType);

        Type csvType = new TypeToken<ArrayList<CsvColumn>>(){}.getType();
        // Convert config-ord.json to Json Object
        List<CsvColumn> csvColumns = JsonUtils.covertJSONToObject(CSV_FILE_PATH,ROOT_NODE_NAME_CSV,csvType);

        String pathFileData = (args==null || args.length==0) ?DATA_FILE_PATH:args[0];
        File fileData = new File(pathFileData);

        if(fileData.isFile() && fileData.exists()) {
            generateCSV(pathFileData,RESULT_CSV_FILE_PATH,recordTypes,csvColumns);
        }else if(fileData.isDirectory()){
            List<String> fileNames = listFileNamesInFolder(pathFileData);

            fileNames.forEach(fd-> generateCSV(fileData+"\\"+fd,fileData+"\\"+generateCSVName(fd),recordTypes,csvColumns));
        }
    }

    private static String generateCSVName(String nameFileData){
        String[] partsName = nameFileData.split("\\.");
        return partsName.length==0? (new Date()).getTime()+CSV_EXTENSION: partsName[0]+CSV_EXTENSION;
    }

    private static List<String> listFileNamesInFolder(String folder) {
        List<String> files = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
            paths
                .filter(Files::isRegularFile)
                .filter(p->p.toString().endsWith(ORD_EXTENSION))
                .forEach(p ->files.add(p.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }

    private static void generateCSV(String pathFileData,String pathFileCSV, List<RecordType> recordTypes,List<CsvColumn> csvColumns){
        // create string like CSV from data matching with CSV.Config
        List<Map<String,String>> csvString = OrdUtils.generateFromOrdDataWithConfig(pathFileData,recordTypes,csvColumns);

        // create new csv file from list string.
        CsvUtils.CreateCSVFile(pathFileCSV,csvString);
    }
}
