package jhn.handlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonHandler {
    private String filepath;
    private JSONObject data;

    public JsonHandler(String filepath) {
        this.filepath = filepath;
        this.data = loadJson();
    }

    // Load existing JSON file
    private JSONObject loadJson() {
        JSONParser parser = new JSONParser();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            JSONObject json = (JSONObject) parser.parse(reader);
            reader.close();
            return json;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    // Save JSON to file
    private void saveJson() {
        try {
            FileWriter writer = new FileWriter(filepath);
            writer.write(data.toJSONString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get integer value
    public int getInt(String key) {
        Object value = data.get(key);
        if (value instanceof Long) {
            return ((Long) value).intValue();
        }
        return 0;
    }

    // Get string value
    public String getString(String key) {
        return (String) data.get(key);
    }

    // gets a boolean value
    public boolean getBoolean(String key) {
        Object value = data.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return false; // default value
    }

    public double getDouble(String key) {
        Object value = data.get(key);
        if (value instanceof Double) {
            return (Double) value;
        }
        return 0.0;

    }

    // Set value
    public void setValue(String key, Object value) {
        data.put(key, value);
        saveJson();
    }

    // Get entire JSON object
    public JSONObject getData() {
        return data;
    }

    public static void main(String[] args) {
        JsonHandler json = new JsonHandler("weatherapp1\\src\\main\\java\\jhn\\settings.json");
        json.setValue("celcius", true);
        System.out.println(json.getBoolean("celcius"));

    }
}