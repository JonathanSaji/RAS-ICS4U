package jhn.run;

import jhn.API.Weather;
import jhn.handlers.BackgroundHandler;
import jhn.handlers.JsonHandler;
import jhn.handlers.SongHandler;
import jhn.location.CurrentLocation;
import jhn.ui.Menu;

public class WeatherApp {
    public static Menu menu;
    public static JsonHandler json;
    public static SongHandler song;
    public static CurrentLocation currentLocation;
    public static BackgroundHandler backgroundHandler;

    public static void main(String[] args) throws Exception {

        song = new SongHandler("weatherapp1/src/main/java/jhn/resources/KCDII.wav");

    
        json = new JsonHandler("weatherapp1\\src\\main\\java\\jhn\\settings.json");
        
        Weather weather = new Weather(getLat(), getLong());
        //Weather weather = new Weather(-78.45, 106.87);

        backgroundHandler = new BackgroundHandler(weather);

        menu = new Menu(weather);
        menu.setVisible(true);

        currentLocation = new CurrentLocation();
    }

    // overloaded constructed so we can use static helper methods
    public WeatherApp() {
    }

    /*
     * Global Helper Variables
     */

    public static int getMiddleX(int sizeDiff) {
        return (1920 - sizeDiff) / 2;
    }

    public static int getMiddleY(int sizeDiff) {
        return (1080 - sizeDiff) / 2;
    }

    public static SongHandler getSongHandler() {
        return song;
    }

    public static double getLat() {
        return getJsonHandler().getDouble("latitude");
    }

    public static double getLong() {
        return getJsonHandler().getDouble("longitude");
    }

    public static void setLat(double lat) {
        if(lat > 90) lat = 90;
        if(lat < -90) lat = -90;
        json.setValue("latitude", lat);
    }

    public static void setLong(double lon) {
        if(lon > 180) lon = 180;
        if(lon < -180) lon = -180;
        json.setValue("longitude", lon);
    }

    public static double getCurrentLong() {
        return currentLocation.getLong();
    }
    public static double getCurrentLat() {
        return currentLocation.getLat();
    }

    public static Menu getMenu() {
        return menu;
    }

    public static JsonHandler getJsonHandler() {
        return json;
    }

    public static BackgroundHandler getBackgroundHandler() {
        return backgroundHandler;
    }

    public static void setBackgroundHandler(BackgroundHandler backgroundHandler) {
        WeatherApp.backgroundHandler = backgroundHandler;
    }

    public static void setMenu(Menu menu) {
        WeatherApp.menu = menu;
    }

    public static SongHandler getMusicHandler() {
        return song;
    }


}
