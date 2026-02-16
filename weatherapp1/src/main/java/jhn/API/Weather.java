package jhn.API;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import jhn.run.WeatherApp;

public class Weather {

    JSONArray time, temperatures, humidity, dewPoint, apparentTemp, precipitation, rain, showers, snowfall, pressureMsl, surfacePressure,
            cloudCover, windSpeed, soilTemp, soilMoisture, isDay, windGusts, windDirection;
    double latitude, longitude;
    JSONObject dataObject;

    public Weather(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        try {
            // Public API
            // https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.41&hourly=temperature_2m&timezone=America%2FNew_York
            // "https://api.open-meteo.com/v1/forecast?latitude=&longitude=-75&hourly=temperature_2m&models=best_match&timezone=America%2FNew_York"

            URL url = new URL(
    "https://api.open-meteo.com/v1/forecast?"
    + "latitude=" + latitude
    + "&longitude=" + longitude
    + "&models=gem_regional"
    + "&hourly=temperature_2m,relative_humidity_2m,dew_point_2m,apparent_temperature,"
    + "precipitation,rain,showers,snowfall,pressure_msl,surface_pressure,cloud_cover,"
    + "wind_speed_10m,soil_temperature_0_to_10cm,soil_moisture_0_to_10cm,is_day,"
    + "wind_gusts_10m,wind_direction_10m"
    + "&timezone=America%2FNew_York"
    + "&past_days=61"
    + "&forecast_days=10"
);

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

                // Change 2: Now you can access keys directly from dataObject
                // System.out.println("Latitude: " + dataObject.get("latitude"));

                // hourly is nested so we are going to need multiple
                JSONObject hourly = (JSONObject) dataObject.get("hourly");
                temperatures = (JSONArray) hourly.get("temperature_2m");
                time = (JSONArray) hourly.get("time");
                humidity = (JSONArray) hourly.get("relative_humidity_2m");
                dewPoint = (JSONArray) hourly.get("dew_point_2m");
                apparentTemp = (JSONArray) hourly.get("apparent_temperature");
                precipitation = (JSONArray) hourly.get("precipitation");
                rain = (JSONArray) hourly.get("rain");
                showers = (JSONArray) hourly.get("showers");
                snowfall = (JSONArray) hourly.get("snowfall");
                pressureMsl = (JSONArray) hourly.get("pressure_msl");
                surfacePressure = (JSONArray) hourly.get("surface_pressure");
                cloudCover = (JSONArray) hourly.get("cloud_cover");
                windSpeed = (JSONArray) hourly.get("wind_speed_10m");
                soilTemp = (JSONArray) hourly.get("soil_temperature_0_to_10cm");
                soilMoisture = (JSONArray) hourly.get("soil_moisture_0_to_10cm");
                isDay = (JSONArray) hourly.get("is_day");
                windGusts = (JSONArray) hourly.get("wind_gusts_10m");
                windDirection = (JSONArray) hourly.get("wind_direction_10m");

                // int index = 10;
                // System.out.println("Temperature: " + temperatures.get(index) + " Time: " +
                // time.get(index));
                // System.out.println("done printing");
            }

            // not the best way to catch but it works!
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public String getLongLat() {
        return "<html>Latitude: " + dataObject.get("latitude") + "<br>Longtitude: " + dataObject.get("longitude")
                + "</html>";
    }

    public String getTime(int slot) {
        return String.valueOf(time.get(slot));
    }


    /*
    Temprature
    */
    // NEW METHOD: Get temperature for a specific date and hour

    public int getAverageTempVal(LocalDate date) {
        int sum = 0;
        int count = 0;
        for (int i = 0; i < 24; i++) { // include hour 0..23
            String val = getValue(temperatures, date, i);
            if (val == null || val.equals("N/A")) continue;
            try {
                // getValue already returns rounded integers or numeric strings; preserve sign
                int temp = Integer.parseInt(val);
                sum += temp;
                count++;
            } catch (NumberFormatException e) {
                try {
                    double d = Double.parseDouble(val);
                    sum += (int) Math.round(d);
                    count++;
                } catch (NumberFormatException ignored) {
                    // skip unparsable values
                }
            }
        }
        return count == 0 ? 0 : sum / count;
    }

    public String getTemperature(LocalDate date, int hour, boolean celcius) {
        String degree = WeatherApp.json.getBoolean("celcius") ? "°C" : "°F";

        if(WeatherApp.json.getBoolean("celcius")){
            return getValue(temperatures, date, hour) + degree;
        }
        else{
            return (int)(Double.parseDouble(getValue(temperatures, date, hour)) * 1.8 + 32) + degree;
        }

    }

    public boolean isThereTemp(LocalDate date, int hour) {
        String searchTime = String.format("%sT%02d:00", date.toString(), hour);
        for (int i = 0; i < time.size(); i++) {
            String timeStr = (String) time.get(i);
            if (timeStr.equals(searchTime)) {
                return true;
            }
        }
        return false;
    }



    /*
    Humidity
    */

    public String getHumidity(LocalDate date, int hour) {
        return "Humidity: " + getValue(humidity, date, hour) + "%";
    }

    public String getDewPoint(LocalDate date, int hour, boolean celcius) {
        String degree = WeatherApp.json.getBoolean("celcius") ? "°C" : "°F";
        return "Dew Point: " + getValue(dewPoint, date, hour) + degree;
    }

    public String getApparentTemp(LocalDate date, int hour, boolean celcius) {
        String degree = WeatherApp.json.getBoolean("celcius") ? "°C" : "°F";

        if(WeatherApp.json.getBoolean("celcius")){
            return "Apparent Temperature: " + getValue(apparentTemp, date, hour) + degree;
        }
        else{
            return "Apparent Temperature: " + (int)(Double.parseDouble(getValue(apparentTemp, date, hour)) * 1.8 + 32) + degree;
        }

    }

    public String getPrecipitation(LocalDate date, int hour) {
        return "Precipitation: " + getValue(precipitation, date, hour) + " mm";
    }

    public String getRain(LocalDate date, int hour) {
        return "Rain: " + getValue(rain, date, hour) + " mm";
    }

    public String getShowers(LocalDate date, int hour) {
        return "Showers: " + getValue(showers, date, hour) + " mm";
    }

    public String getSnowfall(LocalDate date, int hour) {
        return "Snowfall: " + getValue(snowfall, date, hour) + " cm";
    }

    public String getPressureMsl(LocalDate date, int hour) {
        return "Pressure MSL: " + getValue(pressureMsl, date, hour) + " hPa";
    }

    public String getSurfacePressure(LocalDate date, int hour) {
        return "Surface Pressure: " + getValue(surfacePressure, date, hour) + " hPa";
    }

    public String getCloudCover(LocalDate date, int hour) {
        return "Cloud Cover: " + getValue(cloudCover, date, hour) + " %";
    }
    public String getWindSpeed(LocalDate date, int hour) {
        return "Wind Speed: " + getValue(windSpeed, date, hour) + " km/h";
    }

    public String getSoilTemp(LocalDate date, int hour, boolean celcius) {
        String degree = WeatherApp.json.getBoolean("celcius") ? "°C" : "°F";
        return "Soil Temperature: " + getValue(soilTemp, date, hour) + degree;
    }

    public String geSoilMoisture(LocalDate date, int hour) {
        return "Soil Moisture: " + getValue(soilMoisture, date, hour) + " m³/m³";
    }
    public String getIsDay(LocalDate date, int hour) {
        return "Is Day: " + (getValue(isDay, date, hour).equals("1") ? "Yes" : "No");
    }
    public String getWindGusts(LocalDate date, int hour) {
        return "Wind Gusts: " + getValue(windGusts, date, hour) + " km/h";
    }
    public String getWindDirection(LocalDate date, int hour) {
        return "Wind Direction: " + getValue(windDirection, date, hour) + " °";
    }

    public String getValue(JSONArray array, LocalDate date, int hour) {
        String searchTime = String.format("%sT%02d:00", date.toString(), hour);

        for (int i = 0; i < time.size(); i++) {
            String timeStr = (String) time.get(i);
            if (timeStr.equals(searchTime)) {
                Object valueObj = array.get(i);
                if (valueObj instanceof Double) {

                    int value = (int) Math.round((Double) valueObj);
                    return String.valueOf(value);
                }
                else if(valueObj instanceof Long){
                    return String.valueOf(valueObj);
                }
                else if(valueObj instanceof String){
                    return (String) valueObj;
                }
                else if(valueObj == null){
                    return "N/A";
                }
            }
        }

        return null; // Not found
    }

}