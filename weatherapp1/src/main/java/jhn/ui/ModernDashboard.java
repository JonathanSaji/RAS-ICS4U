package jhn.ui;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Consumer;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import jhn.API.Weather;
import jhn.run.WeatherApp;

/** A responsive, keyboard-friendly JavaFX dashboard for Rise & Shine. */
public final class ModernDashboard {
    private static final String BG = "#081426";
    private static final String PANEL = "#10243d";
    private static final String PANEL_2 = "#15304f";
    private static final String TEXT = "#f4f8ff";
    private static final String MUTED = "#91a8c2";
    private static final String ACCENT = "#55d6c2";
    private static final String SUN = "#ffc857";

    private final BorderPane root = new BorderPane();
    private final VBox content = new VBox(24);
    private final Label status = new Label("Ready to refresh");
    private final Label location = new Label("Ottawa, Ontario");
    private final Label dateLabel = new Label();
    private final Label heroTemp = new Label("--°");
    private final Label heroCondition = new Label("Loading your forecast...");
    private final Label heroMeta = new Label("Connecting to Open-Meteo");
    private final FlowPane hourly = new FlowPane(12, 12);
    private final VBox details = new VBox(12);
    private final TextField search = new TextField();
    private final Button unitButton = new Button("°C");
    private Weather weather;
    private LocalDate selectedDate = LocalDate.now();
    private Consumer<LocalDate> dateChanged;

    public ModernDashboard() {
        root.setStyle("-fx-background-color: " + BG + ";");
        root.setTop(buildHeader());
        root.setLeft(buildSidebar());
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle("-fx-background: " + BG + "; -fx-background-color: transparent;");
        content.setPadding(new Insets(30, 42, 42, 42));
        root.setCenter(scroll);
        showLoading();
    }

    public BorderPane getRoot() { return root; }
    public void setDateChanged(Consumer<LocalDate> listener) { dateChanged = listener; }

    public void setWeather(Weather value) {
        weather = value;
        render();
    }

    public void setSelectedDate(LocalDate date) {
        selectedDate = date;
        dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.CANADA)));
        render();
    }

    public void showLoading() {
        content.getChildren().setAll(new VBox(14, heading("Good morning", "Your personalized forecast is on its way."), loadingCard()));
    }

    public void showError(String message) {
        content.getChildren().setAll(new VBox(14, heading("We hit a cloud", "The forecast could not be loaded right now."), card(new VBox(12, label(message, "body"), label("Check your connection and use Refresh to try again.", "muted")))));
        status.setText("Unable to refresh");
    }

    private Node buildHeader() {
        HBox header = new HBox(18);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(22, 36, 18, 36));
        header.setStyle("-fx-background-color: " + BG + ";");
        Label brand = label("RISE & SHINE", "brand");
        Label mark = label("☀", "brand-mark");
        HBox title = new HBox(10, mark, brand);
        title.setAlignment(Pos.CENTER_LEFT);
        search.setPromptText("Search a city or location");
        search.setPrefWidth(280);
        search.setOnAction(e -> status.setText("Location search is ready for the next forecast refresh"));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        unitButton.setOnAction(e -> toggleUnits());
        Button refresh = iconButton("↻  Refresh");
        refresh.setOnAction(e -> { status.setText("Refreshing forecast..."); WeatherApp.reloadWeather(); });
        header.getChildren().addAll(title, spacer, search, unitButton, refresh);
        return header;
    }

    private Node buildSidebar() {
        VBox side = new VBox(10);
        side.setPadding(new Insets(22, 18, 22, 18));
        side.setPrefWidth(210);
        side.setStyle("-fx-background-color: #0b1b31;");
        Label overview = label("OVERVIEW", "eyebrow");
        side.getChildren().addAll(overview, navButton("⌂", "Dashboard", true), navButton("◷", "Hourly forecast", false), navButton("▦", "10-day outlook", false), navButton("⚙", "Preferences", false));
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        side.getChildren().addAll(spacer, new Separator(), label("YOUR LOCATION", "eyebrow"), location, label("Automatic location", "muted"));
        return side;
    }

    private Button navButton(String icon, String text, boolean active) {
        Button button = new Button(icon + "   " + text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setStyle(active ? css("nav-active") : css("nav"));
        button.setOnAction(e -> status.setText(text + " selected"));
        return button;
    }

    private void render() {
        if (weather == null || weather.getTime() == null) { showError("No forecast data was returned."); return; }
        boolean celsius = WeatherApp.json.getBoolean("celcius");
        unitButton.setText(celsius ? "°C" : "°F");
        dateLabel.setText(selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.CANADA)));
        int currentHour = Math.min(LocalDateTime.now().getHour(), 23);
        String temp = safe(() -> weather.getTemperature(selectedDate, currentHour, celsius), "--°");
        heroTemp.setText(temp);
        heroCondition.setText(conditionFor(currentHour));
        heroMeta.setText("Feels like " + safe(() -> weather.getApparentTemp(selectedDate, currentHour, celsius), "--") + "  •  Updated just now");
        hourly.getChildren().clear();
        for (int hour = 0; hour < 24; hour += 3) hourly.getChildren().add(hourCard(hour, celsius));
        details.getChildren().setAll(statGrid(currentHour, celsius));
        content.getChildren().setAll(heading("Good morning", "A clear view of what is ahead."), heroCard(), sectionTitle("Today at a glance", dateLabel), hourly, sectionTitle("Conditions", label("Live observations", "muted")), details);
        status.setText("Forecast updated just now");
        FadeTransition fade = new FadeTransition(Duration.millis(420), content);
        fade.setFromValue(0.2); fade.setToValue(1); fade.play();
    }

    private Node heroCard() {
        VBox left = new VBox(5, label("CURRENT CONDITIONS", "eyebrow"), heroTemp, heroCondition, heroMeta);
        heroTemp.setFont(Font.font("System", FontWeight.BOLD, 72)); heroTemp.setTextFill(Color.web(TEXT));
        heroCondition.setStyle(css("hero-condition")); heroMeta.setStyle(css("muted"));
        VBox right = new VBox(12, weatherOrb(), label("A comfortable day to get outside.", "body")); right.setAlignment(Pos.CENTER);
        HBox box = new HBox(20, left, new Region(), right); HBox.setHgrow(box.getChildren().get(1), Priority.ALWAYS); box.setAlignment(Pos.CENTER_LEFT);
        return card(box, "hero-card");
    }

    private Node weatherOrb() {
        StackPane orb = new StackPane(); Circle circle = new Circle(54, Color.web("#244e6d")); Circle inner = new Circle(35, Color.web(SUN)); Label sun = label("☀", "sun-icon"); orb.getChildren().addAll(circle, inner, sun); return orb;
    }

    private Node hourCard(int hour, boolean celsius) {
        String time = String.format("%02d:00", hour); String temp = safe(() -> weather.getTemperature(selectedDate, hour, celsius), "--°");
        VBox box = new VBox(10, label(time, "muted"), label(hour < 12 ? "☀" : "◐", "weather-icon"), label(temp, "hour-temp"), label(hour < 12 ? "Clear" : "Partly cloudy", "tiny")); box.setAlignment(Pos.CENTER); box.setPrefWidth(112);
        return card(box, "hour-card");
    }

    private GridPane statGrid(int hour, boolean celsius) {
        GridPane grid = new GridPane(); grid.setHgap(12); grid.setVgap(12);
        addStat(grid, 0, 0, "Feels like", safe(() -> weather.getApparentTemp(selectedDate, hour, celsius), "--"), "Thermal comfort");
        addStat(grid, 1, 0, "Humidity", safe(() -> weather.getHumidity(selectedDate, hour), "--"), "Relative humidity");
        addStat(grid, 2, 0, "Wind", safe(() -> weather.getWindSpeed(selectedDate, hour), "--"), "From the west");
        addStat(grid, 0, 1, "Cloud cover", safe(() -> weather.getCloudCover(selectedDate, hour), "--"), "Sky conditions");
        addStat(grid, 1, 1, "Precipitation", safe(() -> weather.getPrecipitation(selectedDate, hour), "--"), "Next 60 minutes");
        addStat(grid, 2, 1, "Pressure", safe(() -> weather.getPressureMsl(selectedDate, hour), "--"), "Sea-level pressure");
        return grid;
    }

    private void addStat(GridPane grid, int col, int row, String name, String value, String hint) { VBox item = new VBox(5, label(name.toUpperCase(), "eyebrow"), label(value, "stat-value"), label(hint, "muted")); grid.add(card(item, "stat-card"), col, row); }
    private Node sectionTitle(String title, Node trailing) { HBox box = new HBox(label(title, "section-title"), new Region(), trailing); HBox.setHgrow(box.getChildren().get(1), Priority.ALWAYS); box.setAlignment(Pos.BASELINE_LEFT); return box; }
    private Node heading(String title, String subtitle) { return new VBox(5, label(title, "page-title"), label(subtitle, "subtitle")); }
    private Node loadingCard() { ProgressIndicator spinner = new ProgressIndicator(); spinner.setPrefSize(48, 48); VBox box = new VBox(16, spinner, label("Fetching the latest conditions...", "body")); box.setAlignment(Pos.CENTER); return card(box, "loading-card"); }
    private Node card(Node child) { return card(child, "card"); }
    private Node card(Node child, String style) { StackPane pane = new StackPane(child); pane.setPadding(new Insets(20)); pane.setStyle(css(style)); return pane; }
    private Button iconButton(String text) { Button b = new Button(text); b.setStyle(css("button")); return b; }
    private Label label(String text, String style) { Label l = new Label(text); l.setStyle(css(style)); l.setWrapText(true); return l; }
    private String css(String style) { return "-fx-text-fill: " + TEXT + ";" + (style.equals("muted") ? "-fx-text-fill: " + MUTED + ";" : "") + (style.equals("brand") ? "-fx-font-size: 18px; -fx-font-weight: bold; -fx-letter-spacing: 2px;" : "") + (style.equals("brand-mark") ? "-fx-text-fill: " + ACCENT + "; -fx-font-size: 25px;" : "") + (style.equals("eyebrow") ? "-fx-text-fill: " + ACCENT + "; -fx-font-size: 11px; -fx-font-weight: bold; -fx-letter-spacing: 1.2px;" : "") + (style.equals("page-title") ? "-fx-font-size: 32px; -fx-font-weight: bold;" : "") + (style.equals("subtitle") ? "-fx-text-fill: " + MUTED + "; -fx-font-size: 15px;" : "") + (style.equals("section-title") ? "-fx-font-size: 19px; -fx-font-weight: bold;" : "") + (style.equals("body") ? "-fx-font-size: 16px;" : "") + (style.equals("hero-condition") ? "-fx-font-size: 24px; -fx-font-weight: bold;" : "") + (style.equals("sun-icon") ? "-fx-font-size: 30px;" : "") + (style.equals("weather-icon") ? "-fx-text-fill: " + SUN + "; -fx-font-size: 26px;" : "") + (style.equals("hour-temp") ? "-fx-font-size: 20px; -fx-font-weight: bold;" : "") + (style.equals("tiny") ? "-fx-text-fill: " + MUTED + "; -fx-font-size: 11px;" : "") + (style.equals("stat-value") ? "-fx-font-size: 22px; -fx-font-weight: bold;" : "") + (style.equals("card") ? "-fx-background-color: " + PANEL + "; -fx-background-radius: 18px;" : "") + (style.equals("hero-card") ? "-fx-background-color: linear-gradient(to right, #163757, #1d4260); -fx-background-radius: 22px;" : "") + (style.equals("hour-card") ? "-fx-background-color: " + PANEL + "; -fx-background-radius: 16px;" : "") + (style.equals("stat-card") ? "-fx-background-color: " + PANEL_2 + "; -fx-background-radius: 16px;" : "") + (style.equals("loading-card") ? "-fx-background-color: " + PANEL + "; -fx-background-radius: 18px; -fx-min-height: 260px;" : "") + (style.equals("button") ? "-fx-background-color: " + ACCENT + "; -fx-text-fill: #062122; -fx-font-weight: bold; -fx-background-radius: 10px;" : "") + (style.equals("nav") ? "-fx-background-color: transparent; -fx-text-fill: " + MUTED + "; -fx-padding: 12px;" : "") + (style.equals("nav-active") ? "-fx-background-color: #173d52; -fx-text-fill: " + TEXT + "; -fx-font-weight: bold; -fx-padding: 12px; -fx-background-radius: 10px;" : ""); }
    private void toggleUnits() { boolean next = !WeatherApp.json.getBoolean("celcius"); WeatherApp.json.setValue("celcius", next); render(); }
    private String conditionFor(int hour) { return hour >= 7 && hour <= 18 ? "Bright and comfortable" : "Calm evening"; }
    private String safe(Value value, String fallback) { try { String result = value.get(); return result == null ? fallback : result; } catch (RuntimeException ex) { return fallback; } }
    @FunctionalInterface private interface Value { String get(); }
}
