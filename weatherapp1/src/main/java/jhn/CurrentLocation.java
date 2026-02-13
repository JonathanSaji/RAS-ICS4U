package jhn;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CurrentLocation {

    JSONObject dataObject;

    public CurrentLocation() {
        try {

            URL url = new URL("http://ip-api.com/json/" + new GetIP().getIP());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // check if the connection is made (debugging)
            int responseCode = conn.getResponseCode();

            // 200 = ok
            if (responseCode != 200) {
                // throw an error
                throw new RuntimeException("HttpResponseCode: " + responseCode);

            } else {

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    // just puts everything in one big line
                    informationString.append(scanner.nextLine());
                }
                // close the scanner
                scanner.close();

                // Change 1: Parse as JSONObject, not JSONArray
                JSONParser parse = new JSONParser();
                dataObject = (JSONObject) parse.parse(String.valueOf(informationString));

                System.out.println("Lat: " + getLat());
                System.out.println("Lon: " + getLon());
            }
            // not the best way to catch but it works!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getLat() {
        return (double) dataObject.get("lat");
    }

    public double getLon() {
        return (double) dataObject.get("lon");
    }

    public static void main(String[] args) {
        new CurrentLocation();
    }
}
