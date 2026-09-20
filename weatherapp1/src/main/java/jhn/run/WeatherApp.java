package jhn.run;

import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.stage.Stage;

import jhn.API.Weather;
import jhn.handlers.BackgroundHandler;
import jhn.handlers.JsonHandler;
import jhn.handlers.SongHandler;
import jhn.location.CurrentLocation;
import jhn.ui.Menu;
import jhn.ui.ModernDashboard;

/** Application entry point for the JavaFX Rise & Shine weather experience. */
public class WeatherApp extends Application {
    public static Menu menu;
    public static JsonHandler json;
    public static SongHandler song;
    public static CurrentLocation currentLocation;
    public static BackgroundHandler backgroundHandler;
    private static Weather weather;
    private static ModernDashboard dashboard;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        json = new JsonHandler(settingsPath());
        dashboard = new ModernDashboard();
        Scene scene = new Scene(dashboard.getRoot(), 1280, 820);
        primaryStage.setTitle("Rise & Shine — Weather, beautifully simplified");
        primaryStage.setMinWidth(980);
        primaryStage.setMinHeight(680);
        primaryStage.setScene(scene);
        primaryStage.show();
        reloadWeather();
    }

    /** Loads weather away from the JavaFX application thread so the UI stays responsive. */
    public static void reloadWeather() {
        if (dashboard == null) return;
        dashboard.showLoading();
        Task<Weather> task = new Task<Weather>() {
            @Override protected Weather call() { return new Weather(getLat(), getLong()); }
        };
        task.setOnSucceeded(event -> {
            weather = task.getValue();
            if (weather == null || weather.getTime() == null) { dashboard.showError("Open-Meteo did not return forecast data."); return; }
            backgroundHandler = new BackgroundHandler(weather);
            dashboard.setWeather(weather);
        });
        task.setOnFailed(event -> dashboard.showError("Could not reach the weather service. Please try again."));
        Thread loader = new Thread(task, "weather-loader");
        loader.setDaemon(true);
        loader.start();
    }

    private static String settingsPath() {
        String[] candidates = { "src/main/java/jhn/configure/settings.json", "weatherapp1/src/main/java/jhn/configure/settings.json" };
        for (String candidate : candidates) if (new File(candidate).exists()) return candidate;
        return candidates[0];
    }

    public static void main(String[] args) { launch(args); }
    public static int getMiddleX(int sizeDiff) { return (1920 - sizeDiff) / 2; }
    public static int getMiddleY(int sizeDiff) { return (1080 - sizeDiff) / 2; }
    public static SongHandler getSongHandler() { return song; }
    public static double getLat() { return getJsonHandler().getDouble("latitude"); }
    public static double getLong() { return getJsonHandler().getDouble("longitude"); }
    public static void setLat(double lat) { json.setValue("latitude", Math.max(-90, Math.min(90, lat))); }
    public static void setLong(double lon) { json.setValue("longitude", Math.max(-180, Math.min(180, lon))); }
    public static double getCurrentLong() { return currentLocation.getLong(); }
    public static double getCurrentLat() { return currentLocation.getLat(); }
    public static Menu getMenu() { return menu; }
    public static JsonHandler getJsonHandler() { return json; }
    public static BackgroundHandler getBackgroundHandler() { return backgroundHandler; }
    public static void setBackgroundHandler(BackgroundHandler value) { backgroundHandler = value; }
    public static void setMenu(Menu value) { menu = value; }
    public static SongHandler getMusicHandler() { return song; }
}
