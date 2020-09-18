package com.lexisnexis.mvr.model;

import com.google.gson.*;
import com.lexisnexis.mvr.config.RecordType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;

public class JsonUtils {
    private static JsonElement convertFileToJSON (String pathJsonFile,String memberName){
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(pathJsonFile));

            return !memberName.isEmpty()?jsonElement.getAsJsonObject().get(memberName):jsonElement;
        } catch (JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T covertJSONToObject(String pathJsonFile,String memberName, Type classOfT){
        if (!pathJsonFile.isEmpty()) {
            try{
                Gson gson = new Gson();
                JsonElement layoutNode = convertFileToJSON(pathJsonFile,memberName);
                if(layoutNode!=null){
                    return gson.fromJson(layoutNode, classOfT);
                }
            }catch (JsonIOException | JsonSyntaxException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
