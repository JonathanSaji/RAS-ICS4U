package jhn;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;





public class Weather {

    JSONArray time, temperatures;
    double latitude, longtitude;
    JSONObject dataObject;
    
    public Weather(double latitude, double longtitude){
        this.latitude = latitude;
        this.longtitude = longtitude;
        try {
            //Public API
            //https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m&timezone=America%2FNew_York
            //"https://api.open-meteo.com/v1/forecast?latitude=&longitude=-75&hourly=temperature_2m&models=best_match&timezone=America%2FNew_York"
            
            URL url = new URL("https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longtitude + "&hourly=temperature_2m&models=best_match&timezone=America%2FNew_York");
        
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //check if the connection is made (debugging)
            int responseCode = conn.getResponseCode();

             //200 = ok
            if(responseCode != 200){
                //throw an error
                throw new RuntimeException("HttpResponseCode: " + responseCode);

            }
            else{

                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                
                while(scanner.hasNext()){
                    //just puts everything in one big line 
                    informationString.append(scanner.nextLine());
                }
                //close the scanner
                scanner.close();

                // Change 1: Parse as JSONObject, not JSONArray
                JSONParser parse = new JSONParser();
                dataObject = (JSONObject) parse.parse(String.valueOf(informationString));

                // Change 2: Now you can access keys directly from dataObject
                // System.out.println("Latitude: " + dataObject.get("latitude"));

                // hourly is nested so we are going to need multiple
                JSONObject hourly = (JSONObject) dataObject.get("hourly");
                temperatures = (JSONArray) hourly.get("temperature_2m");
                
                time = (JSONArray) hourly.get("time");
                // int index = 10;
                // System.out.println("Temperature: " + temperatures.get(index) + " Time: " + time.get(index));
                // System.out.println("done printing");
                

            }


            
        //not the best way to catch but it works!
        } catch (Exception e) {
           
            e.printStackTrace();
        }
        
        }

    public String getWeatherWithTime(int slot){

        return "Temp: " + temperatures.get(slot) + " Time: " + time.get(slot) + " Latitude: " + dataObject.get("latitude") + " Longtitude: " + dataObject.get("longitude");
    }

    public String getTemp(int time){
        return temperatures.get(time) + " °C";
    }

    public String getLongLat(){
        return "<html>Latitude: " + dataObject.get("latitude") + "<br>Longtitude: " + dataObject.get("longitude")+ "</html>";
    }

    public String getTime(int slot){
        return ""+time.get(slot);
    }


}
